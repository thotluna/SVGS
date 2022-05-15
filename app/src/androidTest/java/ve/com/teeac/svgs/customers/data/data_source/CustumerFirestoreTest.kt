package ve.com.teeac.svgs.customers.data.data_source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.entities.*
import ve.com.teeac.svgs.customers.di.CustomerModule
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class CustumerFirestoreTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var remote: CustumerFirestore

    @Inject
    lateinit var populate: PopulateDb

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        populate.init()
    }

    @After
    fun tearDown() = runTest {
        db.terminate()
    }

    @Test
    fun create_one_customer() = runTest {
        val customer = CustomerEntity(
            name = "Test in customer first",
            address = "address test customer first"
        )
        val id = remote.saveCustomer(customer)
        assertNotNull(id)
    }

    @Test
    fun update_one_customer() = runTest {
        val customer = populate.customers[0]
        val customerUpdate = customer.copy(name = "test customer second update")

        remote.saveCustomer(customerUpdate)

        val customerUpdated = remote.getCustomerById(customer.id)
        assertEquals(customerUpdate, customerUpdated)
        assertNotEquals(customer.name, customerUpdated.name)
    }

    @Test
    fun disable_one_customer() = runTest {
        val customers = populate.customers.filter { it.statusCurrent == Status.ACTIVE }
        val customer = customers[0]

        remote.disableCustomer(customer.id)
        val customersDb = remote.getAllCustomers().first()
        val customerDb = remote.getCustomerById(customer.id)

        assertEquals(Status.INACTIVE, customerDb.statusCurrent)
        assertEquals(customers.size - 1, customersDb.size)
    }

    @Test
    fun enable_one_customer() = runTest {
        val customers = populate.customers.filter { it.statusCurrent == Status.ACTIVE }
        val customer = populate.customers.find { it.statusCurrent == Status.INACTIVE }

        remote.enabledCustomer(customer!!.id)
        val customersDb = remote.getAllCustomers().first()
        val customerDb = remote.getCustomerById(customer.id)

        assertEquals(Status.ACTIVE, customerDb.statusCurrent)
        assertEquals(customers.size + 1, customersDb.size)
    }

    @Test
    fun update_customer_add_new_system() = runTest {
        val customer = populate.customers[1]
        val systemAlias = SystemAlias(id = "test1", alias = "test system 1 in customer")

        remote.addSystemToCustomer(customer.id, systemAlias)

        val customerGet = remote.getCustomerById(customer.id)
        assertEquals(customer.listSystems.size + 1, customerGet.listSystems.size)
        assertTrue(customerGet.listSystems.contains(systemAlias))
    }

    @Test
    fun update_old_system_for_one_customer() = runTest {
        val customer = populate.customers.find { it.listSystems.size > 0 }
        val systemOrigin = customer!!.listSystems[0]
        val systemUpdate = systemOrigin!!.copy(alias = "test system 4 updated")

        remote.addSystemToCustomer(customer.id, systemUpdate)

        val customerUpdated = remote.getCustomerById(customer.id)
        assertEquals(customer.listSystems.size, customerUpdated.listSystems.size)
        assertTrue(customerUpdated.listSystems.contains(systemUpdate))
        assertFalse(customerUpdated.listSystems.contains(systemOrigin))
    }

    @Test
    fun remove_syste_to_customer() = runTest {
        val customer = populate.customers.find { it.listSystems.size > 0 }
        val systemOrigin = customer!!.listSystems[0]

        remote.removeSystemToCustomer(customer.id, systemOrigin!!)

        val updateCustomer = remote.getCustomerById(customer.id)
        assertEquals(customer.listSystems.size - 1, updateCustomer.listSystems.size)
        assertFalse(updateCustomer.listSystems.contains(systemOrigin))
    }

    @Test
    fun get_customer_by_id() = runTest {
        val customer = populate.customers[0]

        val customerDb = remote.getCustomerById(customer.id)

        assertEquals(customer.name, customerDb.name)
        assertEquals(customer.id, customerDb.id)
    }

    @Test(expected = ResourceDoesNotExistException::class)
    fun get_customer_by_id_does_not_exist() = runTest {
        val id = "1234"
        val customerGet = remote.getCustomerById(id)
        assertNull(customerGet)
    }

    @Test
    fun get_all_customer_active() = runTest {

        val customers = populate.customers.filter { it.statusCurrent == Status.ACTIVE }

        val listCustomers = remote.getAllCustomers().first()

        assertEquals(customers.size, listCustomers.size)
        assertEquals(
            populate.customers.filter { it.statusCurrent == Status.ACTIVE },
            listCustomers
        )
    }

    @Test
    fun get_all_customer_inactive() = runTest {
        val listCustomers = remote.getAllCustomers(status = Status.INACTIVE).first()
        assertEquals(1, listCustomers.size)
    }
}

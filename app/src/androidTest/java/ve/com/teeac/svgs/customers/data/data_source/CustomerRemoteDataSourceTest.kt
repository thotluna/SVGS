package ve.com.teeac.svgs.customers.data.data_source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.pojo.CustomerPojo
import ve.com.teeac.svgs.customers.data.pojo.MinRecordPojo
import ve.com.teeac.svgs.customers.data.pojo.MinServicePojo
import ve.com.teeac.svgs.customers.di.CustomerModule
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class CustomerRemoteDataSourceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var remote: CustomerRemoteDataSource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        deleteAllCustomers()
        db.terminate()
    }

    @Test
    fun create_one_customer() = runTest {

        val customer = getCustomer()

        val id = remote.saveCustomer(customer)

        assertNotNull(id)
    }

    @Test
    fun update_one_customer() = runTest {

        val customer = getCustomer()

        val id = remote.saveCustomer(customer)

        val customerUpdate = customer.copy(id = id, name = "test 2")

        val idUpdate = remote.saveCustomer(customerUpdate)

        assertEquals(id, idUpdate)
    }

    @Test
    fun get_customer_by_id() = runTest {

        val customer = getCustomer()

        val id = remote.saveCustomer(customer)

        val customerGet = remote.getCustomerById(id)

        println(customerGet)

        assertEquals(customer.name, customerGet.name)
        assertEquals(customer.id, customerGet.id)
    }

    @Test(expected = ResourceDoesNotExistException::class)
    fun get_customer_by_id_does_not_exist() = runTest {

        val id = "123"

        val customerGet = remote.getCustomerById(id)

        assertNull(customerGet)
    }

    @Test
    fun get_all_customer() = runTest {

        for (i in 0..10) {
            val customer = getCustomer(prefix = "all_test")
            remote.saveCustomer(customer)
        }

        val listCustomers = remote.getAllCustomers()

        assertEquals(11, listCustomers.filter { it!!.name == "all_test" }.size)
    }

    @Test
    fun get_all_customer_without_customer() = runTest {

        deleteAllCustomers()

        val listCustomers = remote.getAllCustomers()

        assertEquals(0, listCustomers.size)
    }

    private fun deleteAllCustomers() = runTest {
        db.collection("customers").get().await().forEach {
            it.reference.delete()
        }
    }

    private fun getCustomer(hasId: Boolean = false, prefix: String = ""): CustomerPojo {
        val ramdom = UUID.randomUUID().toString()
        val name = prefix.ifEmpty { "test $ramdom" }
        var customer = CustomerPojo(
            name = name,
            address = "address $ramdom",
            listServices = listOf(MinServicePojo(UUID.randomUUID().toString(), "alias $ramdom")),
            listRecord = listOf(
                MinRecordPojo(
                    UUID.randomUUID().toString(),
                    "location record $ramdom",
                    "ela@gmail.com",
                    createdAt = Date().time
                )
            )
        )
        if (hasId) customer = customer.copy(id = UUID.randomUUID().toString())

        return customer
    }
}

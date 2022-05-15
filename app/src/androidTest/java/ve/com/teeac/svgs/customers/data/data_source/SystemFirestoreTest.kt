package ve.com.teeac.svgs.customers.data.data_source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.entities.ElementEntity
import ve.com.teeac.svgs.customers.data.entities.Status
import ve.com.teeac.svgs.customers.data.entities.SystemEntity
import ve.com.teeac.svgs.customers.di.CustomerModule
import java.time.Instant
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class SystemFirestoreTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var remote: SystemFirestore

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
    fun save_new_system() = runTest {
        val customer = populate.customers[0]
        val system =
            SystemEntity(idCustomer = customer.id, name = "New System", alias = "new_system")

        val id = remote.saveSystem(system)

        assertNotNull(id)
    }

    @Test
    fun update_old_system() = runTest {
        val system = populate.systems[0].copy(alias = "New System")
        val id = remote.saveSystem(system)
        assertNotNull(id)

        val systemUpdate = db.collection(SYSTEMS_COLLECTION).document(id).get().await()
            .toObject(SystemEntity::class.java)

        assertEquals(system, systemUpdate)
    }

    @Test
    fun update_last_visit() = runTest {
        val system = populate.systems[0]
        val date = Instant.now().toEpochMilli()

        remote.updateLastVisit(id = system.id, lastVisit = date)

        val systemUpdate = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)

        assertEquals(date, systemUpdate?.lastVisit)
    }

    @Test
    fun disable_system() = runTest {
        val system = populate.systems[0]

        remote.disableSystem(id = system.id)

        val systemUpdated = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)
        assertEquals(Status.INACTIVE, systemUpdated?.statusCurrent)
    }

    @Test
    fun enable_system() = runTest {
        val system = populate.systems.find { it.statusCurrent == Status.INACTIVE }

        remote.enableSystem(id = system!!.id)

        val systemUpdated = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)
        assertEquals(Status.ACTIVE, systemUpdated?.statusCurrent)
    }

    @Test
    fun update_element() = runTest {
        val element = populate.elements.first()
        val newElement = element.copy(alias = "New Element")
        val system = populate.systems.first()

        remote.addElement(system = system, element = newElement.convertMin())

        val systemUpdated = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)
        assertEquals(system.listElements.size, systemUpdated?.listElements?.size)
        assertNotNull(systemUpdated?.listElements?.find { it?.id == element.id })
    }

    @Test
    fun add_new_element() = runTest {
        val newElement = ElementEntity(id = "Other new element", name = "New Element", alias = "new_element")
        val system = populate.systems.first()

        remote.addElement(system = system, element = newElement.convertMin())

        val systemUpdated = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)
        assertNotNull(systemUpdated?.listElements?.find { it?.id == newElement.id })
    }

    @Test
    fun delete_element() = runTest {
        val element = populate.elements.first()
        val system = populate.systems.find { it.id == element.idSystem }!!

        remote.deleteElement(system = system, element = element.convertMin())

        val systemUpdated = db.collection(SYSTEMS_COLLECTION).document(system.id).get().await()
            .toObject(SystemEntity::class.java)
        assertNull(systemUpdated?.listElements?.find { it?.id == element.id })
    }

    @Test
    fun get_system_by_id() = runTest {
        val system = populate.systems.random()
        val systemDb = remote.getSystemById(id = system.id)
        assertEquals(system, systemDb)
    }

    @Test(expected = ResourceDoesNotExistException::class)
    fun get_system_does_not_exist() = runTest {
        remote.getSystemById(id = 10000.toString())
    }

    @Test
    fun get_all_system_active() = runTest {
        val systems = populate.systems.filter { it.statusCurrent == Status.ACTIVE }
        val systemsDb = remote.getAllSystems().first()
        assertEquals(systems, systemsDb)
    }

    @Test
    fun get_all_system_inactive() = runTest {
        val systems = populate.systems.filter { it.statusCurrent == Status.INACTIVE }
        val systemsDb = remote.getAllSystems(status = Status.INACTIVE).first()
        assertEquals(systems, systemsDb)
    }

    @Test
    fun get_systems_by_customer() = runTest {
        val customer = populate.customers.find { it.listSystems.size > 0 }!!
        val systems = remote.getAllSystemByCustomer(idCostumer = customer.id).first()
        assertEquals(customer.listSystems, systems.map { it?.convertSystemAlias() })
    }

    @Test
    fun get_systems_by_customer_does_not_exist() = runTest {
        val systems = remote.getAllSystemByCustomer(idCostumer = "10000").first()
        assertTrue(systems.isEmpty())
    }
}

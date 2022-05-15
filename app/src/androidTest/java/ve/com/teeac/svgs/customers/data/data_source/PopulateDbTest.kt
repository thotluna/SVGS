package ve.com.teeac.svgs.customers.data.data_source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.svgs.customers.data.entities.CustomerEntity
import ve.com.teeac.svgs.customers.data.entities.ElementEntity
import ve.com.teeac.svgs.customers.data.entities.SettingEntity
import ve.com.teeac.svgs.customers.data.entities.SystemEntity
import ve.com.teeac.svgs.customers.di.CustomerModule
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class PopulateDbTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var populate: PopulateDb

    private val itemsCustomer = 2
    private val itemsSystem = 4
    private val itemsElement = 6
    private val itemsSetting = 10

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        db.terminate()
        Dispatchers.resetMain()
    }

    @Test
    fun initialTest() = runTest {
        populate.init()
        val customers = populate.customers
        val systems = populate.systems
        val elements = populate.elements
        val settings = populate.settings

        assert(customers.isNotEmpty())
        assert(systems.isNotEmpty())
        assert(elements.isNotEmpty())
        assert(settings.isNotEmpty())

        val customerWithSystem = customers.find { it.listSystems.size > 0 }
        assertNotNull(customerWithSystem)

        val systemWithElement = systems.find { it.listElements.size > 0 }
        assertNotNull(systemWithElement)

        val settingWithElement = settings.filter { it.idElement.isNotEmpty() }
        assertEquals(settings.size, settingWithElement.size)
    }

    @Test
    fun destroyAll() = runTest {
        populate.destroy()

        val customer =
            db.collection(CUSTOMERS_COLLECTION).get().await().toObjects(CustomerEntity::class.java)
        val systems =
            db.collection(SYSTEMS_COLLECTION).get().await().toObjects(SystemEntity::class.java)
        val elements =
            db.collection(ELEMENTS_COLLECTION).get().await().toObjects(ElementEntity::class.java)
        val settings =
            db.collection(SETTING_COLLECTION).get().await().toObjects(SettingEntity::class.java)

        assertTrue(customer.isEmpty())
        assertTrue(systems.isEmpty())
        assertTrue(elements.isEmpty())
        assertTrue(settings.isEmpty())
    }

    @Test
    fun saveCustomer() = runTest {
        populate.destroy()
        onSaveCustomers(itemsCustomer - 1)
        assertTrue(populate.customers.size >= itemsCustomer)
    }

    @Test
    fun saveSystems() = runTest {
        populate.destroy()
        val timeAll = measureTimeMillis {
            onSaveCustomers(itemsCustomer - 1)
            onSaveSystems(itemsSystem - 1)
        }
        assertTrue(populate.customers.size == itemsCustomer)
        assertTrue(populate.systems.size == itemsSystem)
        println("timeAsync: $timeAll")
    }

    @Test
    fun saveElements() = runTest {
        populate.destroy()
        val totalTime = measureTimeMillis {
            onSaveCustomers(itemsCustomer - 1)
            onSaveSystems(itemsSystem - 1)
            onSaveElements(itemsElement)
        }
        println("timeTotal: $totalTime")
        assertEquals(itemsCustomer, populate.customers.size)
        assertEquals(itemsSystem, populate.systems.size)
        assertEquals(itemsElement, populate.elements.size)
    }

    @Test
    fun saveSettings() = runTest {
        populate.destroy()
        val timerAll = measureTimeMillis {
            onSaveCustomers(itemsCustomer - 1)
            onSaveSystems(itemsSystem - 1)
            onSaveElements(itemsElement)
            onSaveSettings(itemsSetting)
        }
        println("timerAll: $timerAll")
        assertEquals(populate.customers.size, itemsCustomer)
        assertEquals(populate.systems.size, itemsSystem)
        assertEquals(populate.elements.size, itemsElement)
        assertEquals(populate.settings.size, itemsSetting)
    }

    private suspend fun onSaveCustomers(items: Int) {
        val timeSaveCustomer = measureTimeMillis {
            populate.saveListCustomerDb(populate.createListCustomers(items))
        }
        println("timeSaveCustomer: $timeSaveCustomer")
    }

    private suspend fun onSaveSystems(items: Int) {
        val timeSaveSystem = measureTimeMillis {
            populate.saveListSystemDb(populate.createListSystems(items))
        }
        println("timeSaveSystem: $timeSaveSystem")
    }

    private suspend fun onSaveElements(items: Int) {
        val timeSaveElement = measureTimeMillis {
            populate.saveListElementDb(populate.createListElements(items))
        }
        println("timeSaveElement: $timeSaveElement")
    }

    private suspend fun onSaveSettings(items: Int) {
        val timeSaveSetting = measureTimeMillis {
            populate.saveListSettingDb(populate.createListSettings(items))
        }
        println("timeSaveSetting: $timeSaveSetting")
    }
}

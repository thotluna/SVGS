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
import ve.com.teeac.svgs.customers.data.entities.SettingEntity
import ve.com.teeac.svgs.customers.di.CustomerModule
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class SettingFirestoreTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var remote: SettingFirestore

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

    @Test(expected = IllegalArgumentException::class)
    fun save_new_setting_without_element() = runTest {
        val setting = SettingEntity(key = "Frecuencia", value = "305")
        remote.saveSetting(setting)
    }

    @Test
    fun add_new_setting() = runTest {
        val element = populate.elements.first()
        val setting = SettingEntity(key = "Value", value = "158", idElement = element.id)

        val setingSave = remote.saveSetting(setting)

        assertNotNull(setingSave)
        assertEquals(setingSave.key, setting.key)
        assertEquals(setingSave.value, setting.value)
        assertEquals(setingSave.idElement, setting.idElement)

        val listSetings = remote.getAllSettingsByElement(element.id).first()
        assertEquals(listSetings.first { it.id == setingSave.id }, setingSave)
    }

    @Test
    fun update_setting() = runTest {
        val setting = populate.settings.first()

        val settingUpdated = remote.saveSetting(setting.copy(value = "158"))

        assertNotEquals(settingUpdated.value, setting.value)
        assertEquals(settingUpdated.key, setting.key)
        assertEquals(settingUpdated.idElement, setting.idElement)
        assertEquals(settingUpdated.id, setting.id)

        val listSetings = remote.getAllSettingsByElement(setting.idElement).first()
        assertEquals(listSetings.first { it.id == settingUpdated.id }, settingUpdated)
    }

    @Test
    fun get_setting_by_id() = runTest {
        val setting = populate.settings.first()

        val settingGet = remote.getSettingById(setting.id)

        assertNotNull(settingGet)
        assertEquals(settingGet.key, setting.key)
        assertEquals(settingGet.value, setting.value)
        assertEquals(settingGet.idElement, setting.idElement)
        assertEquals(settingGet.id, setting.id)
    }

    @Test
    fun get_all_settings_by_element() = runTest {
        val idElement = populate.settings.first().idElement
        val settings = remote.getAllSettingsByElement(idElement).first()
        assertEquals(
            populate.settings.filter { it.idElement == idElement },
            settings
        )
    }
}

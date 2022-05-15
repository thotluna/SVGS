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
import ve.com.teeac.svgs.customers.data.entities.ElementEntity
import ve.com.teeac.svgs.customers.di.CustomerModule
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(CustomerModule::class)
@RunWith(AndroidJUnit4::class)
class ElementFirestoreTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var remote: ElementFirestore

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

    @Test(expected = AssertionError::class)
    fun add_new_element_without_id_system() = runTest {
        val element = ElementEntity(name = "New Element", description = "New Element Description")
        remote.saveElement(element)
    }

    @Test
    fun add_new_element() = runTest {
        val system = populate.systems.first()
        val element = ElementEntity(
            name = "New Element", description = "New Element Description", idSystem = system.id
        )

        val elementUpdate = remote.saveElement(element)

        assertNotNull(elementUpdate)
    }

    @Test
    fun update_element_exist() = runTest {
        val element = populate.elements.first()
        val newElement = element.copy(name = "New Element")
        val elementUpdated = remote.saveElement(newElement)
        assertEquals(element.id, elementUpdated.id)
        assertEquals(newElement.name, elementUpdated.name)
        assertEquals(newElement.description, elementUpdated.description)
        assertEquals(newElement.idSystem, elementUpdated.idSystem)
    }

    @Test
    fun add_new_element_to_element() = runTest {
        val element = populate.elements.first()
        val elementToElement = ElementEntity(
            name = "New Element aasdasd456454",
            description = "New Element Description",
            idSystem = element.idSystem
        )

        remote.addElementToElement(element.id, elementToElement)

        val elementUpdated = db.collection(ELEMENTS_COLLECTION).document(element.id).get().await()
            .toObject(ElementEntity::class.java)

        assertNotNull(elementUpdated?.elements?.find { it?.name == elementToElement.name })
    }

    @Test
    fun delete_element_from_element() = runTest {
        val element = populate.elements.first()
        val elementToElement = populate.elements.last()
        remote.addElementToElement(element.id, elementToElement)

        var elementUpdated = db.collection(ELEMENTS_COLLECTION).document(element.id).get().await()
            .toObject(ElementEntity::class.java)

        assertNotNull(elementUpdated?.elements?.find { it?.id == elementToElement.id })

        remote.deleteElementFromElement(element.id, elementToElement.convertMin())

        elementUpdated = db.collection(ELEMENTS_COLLECTION).document(element.id).get().await()
            .toObject(ElementEntity::class.java)

        assertNull(elementUpdated?.elements?.find { it?.id == elementToElement.id })
    }

    @Test
    fun get_element_by_id() = runTest {
        val element = populate.elements.first()
        val elementDb = remote.getElementById(element.id)
        assertEquals(element, elementDb)
    }

    @Test
    fun get_all_elements_by_system_id() = runTest {
        val element = populate.elements.first()
        val elements = remote.getAllElementsBySystemId(element.idSystem).first()
        assertEquals(populate.elements.filter { it.idSystem == element.idSystem }, elements)
    }

    @Test
    fun get_all_elements_by_system_id_bad() = runTest {
        val elements = remote.getAllElementsBySystemId(1000.toString()).first()
        assertTrue(elements.isEmpty())
    }
}

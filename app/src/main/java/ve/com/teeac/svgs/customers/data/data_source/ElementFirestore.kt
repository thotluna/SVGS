package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.customers.data.entities.ElementEntity
import ve.com.teeac.svgs.customers.data.entities.ElementMin
import javax.inject.Inject

const val ELEMENTS_COLLECTION = "elements"

class ElementFirestore @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun saveElement(element: ElementEntity): ElementEntity {
        assert(element.idSystem.isNotEmpty())

        return if (element.id.isNotEmpty()) {
            db.collection(ELEMENTS_COLLECTION).document(element.id).set(element).await()
            element
        } else {
            val data = db.collection(ELEMENTS_COLLECTION).add(element).await()
            data.get().await().toObject(ElementEntity::class.java)!!
        }
    }

    suspend fun addElementToElement(id: String, elementToElement: ElementEntity) {
        val parent = db.collection(ELEMENTS_COLLECTION).document(id).get().await()
            .toObject(ElementEntity::class.java)
        val newElement = saveElement(elementToElement)
        parent?.addElement(newElement.convertMin())
        saveElement(parent!!)
    }

    suspend fun deleteElementFromElement(id: String, elementToElement: ElementMin) {
        val parent = db.collection(ELEMENTS_COLLECTION).document(id).get().await()
            .toObject(ElementEntity::class.java)
        parent?.removeElement(elementToElement)
        saveElement(parent!!)
    }

    suspend fun getElementById(id: String): ElementEntity {
        return db.collection(ELEMENTS_COLLECTION).document(id).get().await()
            .toObject(ElementEntity::class.java)!!
    }

    suspend fun getAllElementsBySystemId(idSystem: String): Flow<List<ElementEntity>> {
        return callbackFlow {
            val listener = db.collection(ELEMENTS_COLLECTION).whereEqualTo("idSystem", idSystem)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        close(exception)
                        throw exception
                    } else {
                        val elements = querySnapshot!!.toObjects(ElementEntity::class.java)
                        if (elements.isNotEmpty()) {
                            trySend(elements)
                        } else {
                            trySend(emptyList())
                        }
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }
}

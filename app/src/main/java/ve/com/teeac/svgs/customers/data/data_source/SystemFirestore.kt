package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.entities.ElementMin
import ve.com.teeac.svgs.customers.data.entities.Status
import ve.com.teeac.svgs.customers.data.entities.SystemEntity
import javax.inject.Inject

const val SYSTEMS_COLLECTION = "systems"

class SystemFirestore @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun saveSystem(system: SystemEntity): String {
        return if (system.id.isNotEmpty()) {
            db.collection(SYSTEMS_COLLECTION).document(system.id).set(system).await()
            system.id
        } else {
            val data = db.collection(SYSTEMS_COLLECTION).add(system).await()
            data.id
        }
    }

    suspend fun updateLastVisit(id: String, lastVisit: Long) {
        db.collection(SYSTEMS_COLLECTION).document(id).update("lastVisit", lastVisit).await()
    }

    suspend fun disableSystem(id: String) {
        db.collection(SYSTEMS_COLLECTION).document(id).update("statusCurrent", Status.INACTIVE).await()
    }

    suspend fun enableSystem(id: String) {
        db.collection(SYSTEMS_COLLECTION).document(id).update("statusCurrent", Status.ACTIVE).await()
    }

    suspend fun addElement(system: SystemEntity, element: ElementMin) {
        system.addElement(element)
        saveSystem(system)
    }

    suspend fun deleteElement(system: SystemEntity, element: ElementMin) {
        system.removeElement(element)
        saveSystem(system)
    }

    suspend fun getSystemById(id: String): SystemEntity {
        return db.collection(SYSTEMS_COLLECTION).document(id).get().await()
            .toObject(SystemEntity::class.java)
            ?: throw ResourceDoesNotExistException("Resource does not exist")
    }

    fun getAllSystems(status: Status = Status.ACTIVE): Flow<List<SystemEntity?>> {
        return callbackFlow {
            val listener = db.collection(SYSTEMS_COLLECTION).whereEqualTo("statusCurrent", status)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        close(exception)
                        throw exception
                    } else {
                        val systems = querySnapshot?.toObjects(SystemEntity::class.java)
                        if (systems != null) {
                            trySend(systems)
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

    fun getAllSystemByCustomer(idCostumer: String): Flow<List<SystemEntity?>> {
        return callbackFlow {
            val listener = db.collection(SYSTEMS_COLLECTION).whereEqualTo("idCustomer", idCostumer)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {

                        cancel(exception.message.toString())
                    } else {
                        val systems = snapshot?.toObjects(SystemEntity::class.java) ?: emptyList()
                        trySend(systems)
                    }
                }
            awaitClose { listener.remove() }
        }
    }
}

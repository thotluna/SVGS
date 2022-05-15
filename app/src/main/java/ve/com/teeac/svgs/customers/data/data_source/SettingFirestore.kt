package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.entities.SettingEntity
import javax.inject.Inject

const val SETTING_COLLECTION = "settings"

class SettingFirestore @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun saveSetting(setting: SettingEntity): SettingEntity {
        if (setting.idElement.isEmpty()) throw IllegalArgumentException("Requerid element asociated")
        return if (setting.id.isNotEmpty()) {
            db.collection(SETTING_COLLECTION).document(setting.id).set(setting).await()
            setting
        } else {
            val data = db.collection(SETTING_COLLECTION).add(setting).await()
            data.get().await().toObject(SettingEntity::class.java)!!
        }
    }

    fun getAllSettingsByElement(idElement: String): Flow<List<SettingEntity>> {
        return callbackFlow {
            val listener = db.collection(SETTING_COLLECTION)
                .whereEqualTo("idElement", idElement)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        cancel(exception.message.toString())
                        throw exception
                    } else {
                        val settings = snapshot?.toObjects(SettingEntity::class.java)
                        trySend(settings ?: emptyList())
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    suspend fun getSettingById(id: String): SettingEntity {
        return db.collection(SETTING_COLLECTION).document(id).get().await()
            .toObject(SettingEntity::class.java)
            ?: throw ResourceDoesNotExistException("Setting does not exist")
    }
}

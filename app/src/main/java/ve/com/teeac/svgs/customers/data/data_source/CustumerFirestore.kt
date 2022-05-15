package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.entities.CustomerEntity
import ve.com.teeac.svgs.customers.data.entities.Status
import ve.com.teeac.svgs.customers.data.entities.SystemAlias
import javax.inject.Inject

const val CUSTOMERS_COLLECTION = "customers"

class CustumerFirestore @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun saveCustomer(customer: CustomerEntity): String {
        return if (customer.id.isNotEmpty()) {
            db.collection(CUSTOMERS_COLLECTION).document(customer.id).set(customer).await()
            customer.id
        } else {
            val data = db.collection(CUSTOMERS_COLLECTION).add(customer).await()
            saveCustomer(customer.copy(id = data.id))
            data.id
        }
    }

    fun getAllCustomers(status: Status = Status.ACTIVE): Flow<List<CustomerEntity?>> {
//        val data = db.collection(CUSTOMERS_COLLECTION).whereEqualTo("statusCurrent", status).get().await()
//        return data.toObjects(CustomerEntity::class.java)
        return callbackFlow {
            val listener = db.collection(CUSTOMERS_COLLECTION).whereEqualTo("statusCurrent", status)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        close(exception)
                        throw exception
                    } else {
                        val customers = querySnapshot?.toObjects(CustomerEntity::class.java)
                        if (customers != null) {
                            trySend(customers)
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

    suspend fun getCustomerById(id: String): CustomerEntity {
        val data = db.collection(CUSTOMERS_COLLECTION).document(id).get().await()
        return data.toObject(CustomerEntity::class.java)
            ?: throw ResourceDoesNotExistException("Resource does not exist")
    }

    suspend fun addSystemToCustomer(idCustumer: String, system: SystemAlias) {
        val customer = getCustomerById(idCustumer)
        customer.addSystem(system)
        saveCustomer(customer)
    }

    suspend fun removeSystemToCustomer(idCustumer: String, system: SystemAlias) {
        val customer = getCustomerById(idCustumer)
        customer.removeSystem(system)
        saveCustomer(customer)
    }

    suspend fun disableCustomer(id: String) {
        db.collection(CUSTOMERS_COLLECTION).document(id).update("statusCurrent", Status.INACTIVE).await()
    }

    suspend fun enabledCustomer(id: String) {
        db.collection(CUSTOMERS_COLLECTION).document(id).update("statusCurrent", Status.ACTIVE).await()
    }
}

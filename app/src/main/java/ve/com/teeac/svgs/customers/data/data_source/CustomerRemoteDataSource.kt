package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.core.exceptions.ResourceDoesNotExistException
import ve.com.teeac.svgs.customers.data.pojo.CustomerPojo
import javax.inject.Inject

const val CUSTOMERS_COLLECTION = "customers"

class CustomerRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun saveCustomer(customer: CustomerPojo): String {
        customer.id?.let {
            db.collection(CUSTOMERS_COLLECTION).document(it).set(customer).await()
            return it
        } ?: run {
            val data = db.collection(CUSTOMERS_COLLECTION).add(customer).await()
            return data.id
        }
    }

    suspend fun getCustomerById(id: String): CustomerPojo {
        val data = db.collection(CUSTOMERS_COLLECTION).document(id).get().await()
        return data.toObject(CustomerPojo::class.java)
            ?: throw ResourceDoesNotExistException("Resource does not exist")
    }

    suspend fun getAllCustomers(): List<CustomerPojo?> {
        val data = db.collection(CUSTOMERS_COLLECTION).get().await()
        return data.toObjects(CustomerPojo::class.java)
    }
}

package ve.com.teeac.svgs.customers.data.data_source

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.customers.data.entities.*
import javax.inject.Inject

class PopulateDb @Inject constructor(private val db: FirebaseFirestore) {

    var customers = mutableListOf<CustomerEntity>()

    var systems = mutableListOf<SystemEntity>()

    var elements = mutableListOf<ElementEntity>()

    var settings = mutableListOf<SettingEntity>()

    suspend fun init() {
        destroy()
        saveListCustomerDb(createListCustomers(1))
        saveListSystemDb(createListSystems(3))
        saveListElementDb(createListElements(6))
        saveListSettingDb(createListSettings(12))

        loadDataEnded()
    }

    private suspend fun loadDataEnded() {
        withContext(Dispatchers.IO) {
            val deferredCustomers = async { loadCustomers() }
            val deferredSystems = async { loadSystems() }
            val deferredElements = async { loadElements() }
            val deferredSettings = async { loadSettings() }

            customers = deferredCustomers.await()
            systems = deferredSystems.await()
            elements = deferredElements.await()
            settings = deferredSettings.await()
        }
    }

    fun createListCustomers(quantity: Int = 1): List<CustomerEntity> {
        val list = mutableListOf<CustomerEntity>()
        for (i in 1..quantity) {
            list.add(
                CustomerEntity(
                    name = "Customer $i",
                    address = "Address $i"
                )
            )
        }
        list.add(
            CustomerEntity(
                name = "Customer ${list.size + 1}",
                address = "Address ${list.size + 1}",
                statusCurrent = Status.INACTIVE
            )
        )
        return list
    }

    private suspend fun saveCustomerDb(customer: CustomerEntity): DocumentReference? {
        return db.collection(CUSTOMERS_COLLECTION).add(customer).await()
    }

    suspend fun saveListCustomerDb(list: List<CustomerEntity>) {
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<DocumentReference?>>()
            list.forEach {
                val result = async { saveCustomerDb(it) }
                listResult.add(result)
            }
            listResult.awaitAll()
            customers = loadCustomers()
        }
    }

    private suspend fun loadCustomers(): MutableList<CustomerEntity> {
        return db.collection(CUSTOMERS_COLLECTION).get().await()
            .toObjects(CustomerEntity::class.java)
    }

    private suspend fun updateCustomersDb(list: List<SystemEntity>) {
        list.forEach { systemEntity ->
            val customer = customers.find { it.id == systemEntity.idCustomer }!!
            customer.addSystem(systemEntity.convertSystemAlias())
        }
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<Void?>>()
            customers.forEach {
                val result = async { updateCustomer(it) }
                listResult.add(result)
            }
            listResult.awaitAll()
            customers = loadCustomers()
        }
    }

    private suspend fun updateCustomer(customer: CustomerEntity): Void? {
        return db.collection(CUSTOMERS_COLLECTION).document(customer.id).set(customer).await()
    }

    fun createListSystems(quantity: Int = 3): List<SystemEntity> {
        val list = mutableListOf<SystemEntity>()
        for (i in 1..quantity) {
            list.add(
                SystemEntity(
                    name = "System $i",
                    alias = "Alias $i",
                )
            )
        }
        list.add(
            SystemEntity(
                name = "System ${list.size + 1}",
                alias = "Alias ${list.size + 1}",
                statusCurrent = Status.INACTIVE
            )
        )
        return list
    }

    private suspend fun saveSystemDb(system: SystemEntity): DocumentReference? {
        return db.collection(SYSTEMS_COLLECTION).add(system).await()
    }

    suspend fun saveListSystemDb(list: List<SystemEntity>) {
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<DocumentReference?>>()
            list.forEach {
                val customer = customers.random()
                val result = async { saveSystemDb(it.copy(idCustomer = customer.id)) }
                listResult.add(result)
            }
            listResult.awaitAll()
            systems = loadSystems()
            updateCustomersDb(systems)
        }
    }

    private suspend fun loadSystems(): MutableList<SystemEntity> {
        return db.collection(SYSTEMS_COLLECTION).get().await()
            .toObjects(SystemEntity::class.java)
    }

    private suspend fun updateSystemDb(system: SystemEntity): Void? {
        return db.collection(SYSTEMS_COLLECTION).document(system.id).set(system).await()
    }

    fun createListElements(quantity: Int = 6): List<ElementEntity> {
        val list = mutableListOf<ElementEntity>()
        for (i in 1..quantity) {
            list.add(
                ElementEntity(
                    name = "Element $i",
                    alias = "Alias $i",
                    brand = "Brand $i",
                    model = "Model $i",
                    description = "decription $i"
                )
            )
        }
        return list
    }

    private suspend fun saveElementDb(element: ElementEntity): DocumentReference? {
        return db.collection(ELEMENTS_COLLECTION).add(element).await()
    }

    suspend fun saveListElementDb(list: List<ElementEntity>) {
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<DocumentReference?>>()
            list.forEach {
                val system = systems.random()
                val result = async { saveElementDb(it.copy(idSystem = system.id)) }
                listResult.add(result)
            }
            listResult.awaitAll()
            elements = loadElements()
            updateSystemWithElementToDb(elements)
        }
    }

    private suspend fun loadElements(): MutableList<ElementEntity> {
        return db.collection(ELEMENTS_COLLECTION).get().await()
            .toObjects(ElementEntity::class.java)
    }

    private suspend fun updateSystemWithElementToDb(list: List<ElementEntity>) {
        val newSystems = mutableListOf<SystemEntity>()
        list.forEach { elementEntity ->
            val system = systems.find { it.id == elementEntity.idSystem }!!
            system.addElement(elementEntity.convertMin())
            newSystems.add(system)
        }
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<Void?>>()
            newSystems.forEach {
                val result = async { updateSystemDb(it) }
                listResult.add(result)
            }
            listResult.awaitAll()
            systems = loadSystems()
        }
    }

    fun createListSettings(quantity: Int = 10): List<SettingEntity> {
        val list = mutableListOf<SettingEntity>()
        for (i in 1..quantity) {
            list.add(
                SettingEntity(
                    key = "Key $i",
                    value = "Value $i"
                )
            )
        }

        return list
    }

    suspend fun saveListSettingDb(list: List<SettingEntity>) {
        withContext(Dispatchers.IO) {
            val listResult = mutableListOf<Deferred<DocumentReference?>>()
            list.forEach {
                val element = elements.random()
                val result = async { saveSettingDb(it.copy(idElement = element.id)) }
                listResult.add(result)
            }
            listResult.awaitAll()
            settings = loadSettings()
        }
    }

    private suspend fun loadSettings(): MutableList<SettingEntity> {
        return db.collection(SETTING_COLLECTION).get().await()
            .toObjects(SettingEntity::class.java)
    }

    private suspend fun saveSettingDb(setting: SettingEntity): DocumentReference? {
        return db.collection(SETTING_COLLECTION).add(setting).await()
    }

    suspend fun destroy() {

        db.collection(CUSTOMERS_COLLECTION).get().await().forEach {
            it.reference.delete()
        }

        db.collection(SYSTEMS_COLLECTION).get().await().forEach {
            it.reference.delete()
        }

        db.collection(ELEMENTS_COLLECTION).get().await().forEach {
            it.reference.delete()
        }

        db.collection(SETTING_COLLECTION).get().await().forEach {
            it.reference.delete()
        }
    }
}

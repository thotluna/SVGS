package ve.com.teeac.svgs.customers.data.entities

import com.google.firebase.firestore.DocumentId

data class CustomerEntity(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val start: Long = 0,
    val type: CustomerType = CustomerType.RECURRENT,
    val statusCurrent: Status = Status.ACTIVE,
    var listSystems: MutableList<SystemAlias?> = mutableListOf(),
    val listRecord: List<RecordMin?> = listOf()
) {
    fun addSystem(system: SystemAlias) {
        this.removeSystem(system)
        listSystems.add(system)
    }

    fun removeSystem(system: SystemAlias) {
        listSystems.find { it?.id == system.id }?.let {
            listSystems.remove(it)
        }
    }
}

data class CustomerMin(
    val id: String? = null,
    val name: String = "",
)

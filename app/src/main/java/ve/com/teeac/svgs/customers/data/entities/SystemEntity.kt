package ve.com.teeac.svgs.customers.data.entities

import com.google.firebase.firestore.DocumentId

data class SystemEntity(
    @DocumentId
    val id: String = "",
    val idCustomer: String = "",
    val name: String = "",
    val alias: String = "",
    val lastVisit: Long = 0,
    val statusCurrent: Status = Status.ACTIVE,
    val listElements: MutableList<ElementMin?> = mutableListOf(),
    val listRecords: MutableList<RecordMin?> = mutableListOf()
) {
    fun convertMin() = SystemMin(id, name, alias, lastVisit, statusCurrent)
    fun convertSystemAlias() = SystemAlias(id, alias, statusCurrent)

    fun addElement(element: ElementMin) {
        this.removeElement(element)
        listElements.add(element)
    }

    fun removeElement(element: ElementMin) {
        listElements.find { it?.id == element.id }?.let {
            listElements.remove(it)
        }
    }
}

data class SystemAlias(
    val id: String? = null,
    val alias: String = "",
    val status: Status = Status.ACTIVE
)

data class SystemMin(
    val id: String = "",
    val name: String = "",
    val alias: String = "",
    val lastVisit: Long = 0,
    val status: Status = Status.ACTIVE
)

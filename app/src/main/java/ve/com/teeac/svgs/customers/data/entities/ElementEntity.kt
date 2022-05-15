package ve.com.teeac.svgs.customers.data.entities

import com.google.firebase.firestore.DocumentId

data class ElementEntity(
    @DocumentId
    val id: String = "",
    val idSystem: String = "",
    val name: String = "",
    val alias: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val description: String? = null,
    val elements: MutableList<ElementMin?> = mutableListOf(),
    val listRecord: MutableList<RecordMin?> = mutableListOf()
) {
    fun convertMin() = ElementMin(id, name, alias)
    fun addElement(element: ElementMin) {
        this.elements.remove(element)
        elements.add(element)
    }
    fun removeElement(element: ElementMin) {
        elements.remove(element)
    }
}

data class ElementMin(
    val id: String = "",
    val name: String = "",
    val alias: String? = null
)

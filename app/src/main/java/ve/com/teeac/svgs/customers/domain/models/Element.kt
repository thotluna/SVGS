package ve.com.teeac.svgs.customers.domain.models

data class Element(
    val id: String = "",
    val idSystem: String = "",
    val name: String,
    val alias: String?,
    val brand: String?,
    val model: String?,
    val description: String?,
    val elements: List<ElementItem?> = listOf(),
    val setings: List<Setting> = listOf(),
    val recordList: List<Record?> = listOf()
)

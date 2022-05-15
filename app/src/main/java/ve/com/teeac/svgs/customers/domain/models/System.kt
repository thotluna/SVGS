package ve.com.teeac.svgs.customers.domain.models

import ve.com.teeac.svgs.customers.data.entities.Status

data class System(
    val id: String = "",
    val idCustomer: String = "",
    val name: String = "",
    val alias: String = "",
    val lastVisit: Long? = null,
    val status: Status = Status.ACTIVE,
    val elementsList: List<ElementItem?> = listOf(),
    val recordList: List<Record?> = listOf()
)

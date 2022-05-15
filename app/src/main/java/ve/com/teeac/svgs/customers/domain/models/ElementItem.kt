package ve.com.teeac.svgs.customers.domain.models

import ve.com.teeac.svgs.customers.data.entities.Status

data class ElementItem(
    val id: String? = null,
    val name: String,
    val alias: String?,
    val status: Status = Status.ACTIVE
)

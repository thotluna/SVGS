package ve.com.teeac.svgs.customers.domain.models

import java.util.*

data class Record(
    val id: String? = null,
    val location: String? = null,
    val details: String = "",
    val createdBy: String = "",
    val createdAt: Long? = null
)

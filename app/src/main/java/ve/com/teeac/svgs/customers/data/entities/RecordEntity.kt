package ve.com.teeac.svgs.customers.data.entities

import java.util.*

data class RecordEntity(
    val id: String? = null,
    val customer: CustomerMin? = null,
    val service: SystemMin? = null,
    val element: ElementMin? = null,
    val details: String = "",
    val createdBy: String = "",
    val createdAt: Long = Date().time
) {
    fun convertMin(): RecordMin {
        var location = ""
        customer?.let {
            location += "$it "
        }
        service?.let {
            location += "$it "
        }
        element?.let {
            location += "$it"
        }
        return RecordMin(
            id = id!!,
            location = location.trim(),
            createdBy = createdBy,
            createdAt = createdAt
        )
    }
}

data class RecordMin(

    val id: String? = null,
    val location: String? = null,
    val createdBy: String = "",
    val createdAt: Long = 0
)

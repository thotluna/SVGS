package ve.com.teeac.svgs.customers.data.pojo

import java.util.*

data class RecordPojo(
    val id: String? = null,
    val customer: MinCustomerPojo? = null,
    val service: MinServicePojo? = null,
    val element: MinElementPojo? = null,
    val details: String = "",
    val createdBy: String = "",
    val createdAt: Long = Date().time
) {
    fun convertMin(): MinRecordPojo {
        var location = ""
        customer?.let {
            location += "$it "
        }
        service?.let {
            location += "$it "
        }
        element?.let {
            location += "$it "
        }
        return MinRecordPojo(
            id = id!!,
            location = location.trim(),
            createdBy = createdBy,
            createdAt = createdAt
        )
    }
}

data class MinRecordPojo(

    val id: String? = null,
    val location: String? = null,
    val createdBy: String = "",
    val createdAt: Long = 0
)

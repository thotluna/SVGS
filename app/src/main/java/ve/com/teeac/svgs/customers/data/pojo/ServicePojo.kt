package ve.com.teeac.svgs.customers.data.pojo

data class ServicePojo(
    val id: String? = null,
    val name: String,
    val alias: String,
    val ListInvetory: List<MinElementPojo>?,
    val ListRecords: List<MinRecordPojo>
) {
    fun convertMin() = MinServicePojo(id!!, alias)
}

data class MinServicePojo(
    val id: String = "",
    val alias: String = ""
)

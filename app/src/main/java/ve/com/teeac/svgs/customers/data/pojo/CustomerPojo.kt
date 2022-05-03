package ve.com.teeac.svgs.customers.data.pojo

data class CustomerPojo(
    val id: String? = null,
    val name: String = "",
    val address: String = "",
    val listServices: List<MinServicePojo?> = listOf(),
    val listRecord: List<MinRecordPojo?> = listOf()
)

data class MinCustomerPojo(
    val id: String,
    val name: String
)

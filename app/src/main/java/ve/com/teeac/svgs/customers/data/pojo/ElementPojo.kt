package ve.com.teeac.svgs.customers.data.pojo

data class ElementPojo(
    val id: String? = null,
    val customer: MinCustomerPojo,
    val service: MinServicePojo,
    val name: String,
    val alias: String?,
    val marca: String?,
    val model: String?,
    val description: String?,
    val elements: List<MinElementPojo>?,
    val listRecord: List<MinRecordPojo>? = null
) {
    fun convertMin() = MinElementPojo(id!!, name)
}

data class MinElementPojo(
    val id: String,
    val name: String
)

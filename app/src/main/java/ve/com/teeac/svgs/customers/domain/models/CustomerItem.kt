package ve.com.teeac.svgs.customers.domain.models

data class CustomerItem(
    val id: String,
    val name: String,
    val address: String,
    val listSystems: MutableList<SystemAliasItem>
)

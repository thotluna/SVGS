package ve.com.teeac.svgs.customers.domain.models

import ve.com.teeac.svgs.customers.data.entities.CustomerType
import ve.com.teeac.svgs.customers.data.entities.Status
import java.util.*

data class Customer(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val start: Long = Date().time,
    val type: CustomerType = CustomerType.RECURRENT,
    val status: Status = Status.ACTIVE,
    val systemList: List<SystemMinItem?> = listOf(),
    val recordList: List<Record?> = listOf()
)

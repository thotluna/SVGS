package ve.com.teeac.svgs.customers.data.entities

import com.google.firebase.firestore.DocumentId

data class SettingEntity(
    @DocumentId
    val id: String = "",
    val idElement: String = "",
    val value: String = "",
    val key: String = ""
)

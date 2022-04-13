package ve.com.teeac.svgs.authentication.data.models

import java.io.Serializable

data class User(
    val displayName: String?,
    val email: String?,
    val token: String?,
) : Serializable

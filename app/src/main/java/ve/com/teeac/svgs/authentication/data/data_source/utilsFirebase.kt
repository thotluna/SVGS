package ve.com.teeac.svgs.authentication.data.data_source

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.UserInfo

suspend fun convertFirebaseUserToUserInfo(user: FirebaseUser): UserInfo {
    val checkToken = user.getIdToken(true).await()
    return UserInfo(
        displayName = user.displayName,
        email = user.email,
        token = checkToken.token
    )
}

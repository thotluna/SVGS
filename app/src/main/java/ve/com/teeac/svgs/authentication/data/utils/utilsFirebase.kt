package ve.com.teeac.svgs.authentication.data.utils

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.User

suspend fun FirebaseUser.convertFirebaseUserToUserInfo(): User {
    val checkToken = getIdToken(true).await()
    return User(
        displayName = displayName,
        email = email,
        token = checkToken.token
    )
}

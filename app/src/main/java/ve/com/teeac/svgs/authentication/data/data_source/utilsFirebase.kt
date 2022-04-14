package ve.com.teeac.svgs.authentication.data.data_source

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.User

suspend fun convertFirebaseUserToUserInfo(user: FirebaseUser): User {
    val checkToken = user.getIdToken(true).await()
    return User(
        displayName = user.displayName,
        email = user.email,
        token = checkToken.token
    )
}

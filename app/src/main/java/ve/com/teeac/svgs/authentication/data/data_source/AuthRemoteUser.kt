package ve.com.teeac.svgs.authentication.data.data_source

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.models.User

interface AuthRemoteUser {
    val externalScope: CoroutineScope

    suspend fun signUpByEmailAndPassword(email: String, password: String): User?

    suspend fun signInByEmailAndPassword(email: String, password: String): User?

    fun signOut()

    fun authStateChanges(): SharedFlow<User?>

    suspend fun authenticationWithCredential(idToken: String, accessToken: String?): User?
}

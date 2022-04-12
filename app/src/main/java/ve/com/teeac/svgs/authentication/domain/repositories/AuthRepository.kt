package ve.com.teeac.svgs.authentication.domain.repositories

import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.data_source.Credentials
import ve.com.teeac.svgs.authentication.data.models.UserInfo

interface AuthRepository {
    suspend fun signUpByEmailAndPassword(email: String, password: String): UserInfo?

    suspend fun signInByEmailAndPassword(email: String, password: String): UserInfo?
    fun authStateChanges(): SharedFlow<UserInfo?>
    fun signOut()
    suspend fun signInWithGoogle(credentials: Credentials): UserInfo?
}

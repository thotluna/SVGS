package ve.com.teeac.svgs.authentication.domain.repositories

import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.data_source.Credentials
import ve.com.teeac.svgs.authentication.data.models.User

interface AuthRepository {
    suspend fun signUpByEmailAndPassword(email: String, password: String): User?

    suspend fun signInByEmailAndPassword(email: String, password: String): User?
    fun authStateChanges(): SharedFlow<User?>
    fun signOut()
    suspend fun signInWithGoogle(credentials: Credentials): User?
}

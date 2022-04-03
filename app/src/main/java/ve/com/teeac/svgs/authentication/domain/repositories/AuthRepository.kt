package ve.com.teeac.svgs.authentication.domain.repositories

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.models.UserInfo

interface AuthRepository {
    suspend fun signUpByEmailAndPassword(email: String, password: String): UserInfo?

    suspend fun signInByEmailAndPassword(email: String, password: String): UserInfo?
    fun authStateChanges(): SharedFlow<UserInfo?>
    fun signOut()
    suspend fun signInWithGoogle(task: Task<GoogleSignInAccount>?)
}

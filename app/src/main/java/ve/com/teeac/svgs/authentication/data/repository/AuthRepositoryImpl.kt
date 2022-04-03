package ve.com.teeac.svgs.authentication.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val repositoryRemote: AuthRemoteUser
) : AuthRepository {
    override suspend fun signUpByEmailAndPassword(email: String, password: String): UserInfo? {
        return repositoryRemote.signUpByEmailAndPassword(email, password)
    }

    override suspend fun signInByEmailAndPassword(email: String, password: String): UserInfo? {
        return repositoryRemote.signInByEmailAndPassword(email, password)
    }

    override fun authStateChanges(): SharedFlow<UserInfo?> {
        return repositoryRemote.authStateChanges()
    }

    override fun signOut() {
        repositoryRemote.signOut()
    }

    override suspend fun signInWithGoogle(task: Task<GoogleSignInAccount>?) {
        repositoryRemote.signInWithGoogle(task)
    }
}

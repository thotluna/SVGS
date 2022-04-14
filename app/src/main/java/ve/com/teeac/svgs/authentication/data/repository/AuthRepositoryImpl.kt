package ve.com.teeac.svgs.authentication.data.repository

import kotlinx.coroutines.flow.SharedFlow
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.Credentials
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val repositoryRemote: AuthRemoteUser
) : AuthRepository {
    override suspend fun signUpByEmailAndPassword(email: String, password: String): User? {
        return repositoryRemote.signUpByEmailAndPassword(email, password)
    }

    override suspend fun signInByEmailAndPassword(email: String, password: String): User? {
        return repositoryRemote.signInByEmailAndPassword(email, password)
    }

    override fun authStateChanges(): SharedFlow<User?> {
        return repositoryRemote.authStateChanges()
    }

    override fun signOut() {
        repositoryRemote.signOut()
    }

    override suspend fun signInWithGoogle(credentials: Credentials): User? {
        return repositoryRemote.authenticationWithCredential(credentials.idToken, credentials.accessToken)
    }
}

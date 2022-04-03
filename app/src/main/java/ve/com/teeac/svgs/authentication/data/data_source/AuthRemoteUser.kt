package ve.com.teeac.svgs.authentication.data.data_source

import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCreationException
import javax.inject.Inject

class AuthRemoteUser @Inject constructor(
    private val auth: FirebaseAuth
) {

    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    suspend fun signUpByEmailAndPassword(email: String, password: String): UserInfo {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            if (auth.currentUser == null) throw AuthenticationException("Fail create user")

            return createUserInfo(auth.currentUser!!)
        } catch (e: FirebaseAuthUserCollisionException) {
            throw UserCreationException("The user is already registered.")
        }
    }

    suspend fun signInByEmailAndPassword(email: String, password: String): UserInfo {

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            if (auth.currentUser == null) throw AuthenticationException("Fail authentication")

            return createUserInfo(auth.currentUser!!)
        } catch (e: FirebaseAuthInvalidUserException) {
            throw CredentialsFailException("Fail credentials")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw CredentialsFailException("Fail credentials")
        }
    }

    fun authStateChanges(): SharedFlow<UserInfo?> {
        return callbackFlow {
            val authStateListener: ((FirebaseAuth) -> Unit) = { auth ->
                Timber.d(auth.currentUser?.uid.toString())
                trySend(auth)
            }
            auth.addAuthStateListener(authStateListener)
            awaitClose { auth.removeAuthStateListener(authStateListener) }
        }.map { authentication ->
            authentication.currentUser?.let { firebaseUser ->
                createUserInfo(firebaseUser)
            }
        }.shareIn(
            scope = externalScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )
    }

    fun signOut() = auth.signOut()

    private suspend fun createUserInfo(firebaseUser: FirebaseUser): UserInfo {
        val checkToken = firebaseUser.getIdToken(true).await()
        return UserInfo(
            displayName = firebaseUser.displayName,
            email = firebaseUser.email,
            token = checkToken.token
        )
    }
}

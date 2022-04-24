package ve.com.teeac.svgs.authentication.data.data_source

import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.utils.convertFirebaseUserToUserInfo
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCollisionException
import javax.inject.Inject

class AuthRemoteFirebase @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRemoteUser {

    override val externalScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun signUpByEmailAndPassword(email: String, password: String): User? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.convertFirebaseUserToUserInfo()
                ?: throw AuthenticationException("Fail create user")
        } catch (e: FirebaseAuthUserCollisionException) {
            throw UserCollisionException("The user is already registered.")
        }
    }

    override suspend fun signInByEmailAndPassword(email: String, password: String): User? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser?.convertFirebaseUserToUserInfo()
                ?: throw AuthenticationException("Fail authentication")
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthenticationException("The user is not registered.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw CredentialsFailException("The password or user is incorrect.")
        }
    }

    override fun authStateChanges(): SharedFlow<User?> {
        return callbackFlow {
            val authStateListener: ((FirebaseAuth) -> Unit) = { auth ->
                trySend(auth)
            }
            auth.addAuthStateListener(authStateListener)
            awaitClose { auth.removeAuthStateListener(authStateListener) }
        }.catch {
            Timber.d("Error. Update current user")
        }.map { authentication ->
            authentication.currentUser?.convertFirebaseUserToUserInfo()
        }.shareIn(
            scope = externalScope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )
    }

    override fun signOut() = auth.signOut()

    override suspend fun authenticationWithCredential(
        idToken: String,
        accessToken: String?
    ): User? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, accessToken)
            auth.signInWithCredential(credential).await()
            auth.currentUser?.convertFirebaseUserToUserInfo()
                ?: throw AuthenticationException("Fail authentication")
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthenticationException("The user is not registered o is disabled")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw CredentialsFailException("The credentials are invalid.")
        } catch (e: FirebaseAuthUserCollisionException) {
            throw UserCollisionException("The user is already registered.")
        }
    }
}

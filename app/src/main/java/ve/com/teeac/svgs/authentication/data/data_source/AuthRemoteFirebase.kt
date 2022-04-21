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
import ve.com.teeac.svgs.core.exceptions.ExceptionManager
import javax.inject.Inject

class AuthRemoteFirebase @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRemoteUser {

    override val externalScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun signUpByEmailAndPassword(email: String, password: String): User? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            if (auth.currentUser == null) throw AuthenticationException("Fail create user")
            auth.currentUser?.convertFirebaseUserToUserInfo()
        } catch (e: FirebaseAuthUserCollisionException) {
            Timber.d("The user is already registered.")
            ExceptionManager.getInstance().setException("The user is already registered.")
            null
        } catch (e: Exception) {
            Timber.d("${e.cause}, message: ${e.message}")
            ExceptionManager.getInstance().setException(e.message!!)
            null
        }
    }

    override suspend fun signInByEmailAndPassword(email: String, password: String): User? {

        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            if (auth.currentUser == null) throw AuthenticationException("Fail authentication")
            auth.currentUser?.convertFirebaseUserToUserInfo()
        } catch (e: FirebaseAuthInvalidUserException) {
            Timber.d("Fail authentication")
            ExceptionManager.getInstance().setException("Fail credentials")
            null
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Timber.d("Fail authentication")
            ExceptionManager.getInstance().setException("Fail credentials")
            null
        } catch (e: Exception) {
            Timber.d("$e, message: ${e.message}")
            ExceptionManager.getInstance().setException(e.message!!)
            null
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

    override suspend fun authenticationWithCredential(idToken: String, accessToken: String?): User? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, accessToken)
            auth.signInWithCredential(credential).await()
            auth.currentUser?.convertFirebaseUserToUserInfo()
        } catch (e: IllegalArgumentException) {
            Timber.d(e.message)
            ExceptionManager.getInstance().setException(e.message!!)
            null
        } catch (e: Exception) {
            Timber.d("${e.cause}, message: ${e.message}")
            ExceptionManager.getInstance().setException("Fail credentials")
            null
        }
    }
}

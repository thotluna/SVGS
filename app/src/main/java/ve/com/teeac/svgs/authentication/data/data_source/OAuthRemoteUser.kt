package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.utils.convertFirebaseUserToUserInfo
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCollisionException

abstract class OAuthRemoteUser(
    private val auth: FirebaseAuth,
    private val provider: OAuthProvider
) {
    suspend fun signIn(activity: Activity): User? {

        return try {
            val result = getTask(activity)
            result.let {
                it.user?.convertFirebaseUserToUserInfo()
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            throw AuthenticationException("Invalid user")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw CredentialsFailException("Invalid credentials")
        } catch (e: FirebaseAuthUserCollisionException) {
            throw UserCollisionException("User already exists")
        } catch (e: FirebaseAuthWebException) {
            throw AuthenticationException("Web exception")
        } catch (e: FirebaseAuthException) {
            throw AuthenticationException("Unknown exception")
        }
    }

    private suspend fun getTask(activity: Activity): AuthResult {

        return auth.pendingAuthResult?.await()
            ?: auth.startActivityForSignInWithProvider(activity, provider).await()
    }
}

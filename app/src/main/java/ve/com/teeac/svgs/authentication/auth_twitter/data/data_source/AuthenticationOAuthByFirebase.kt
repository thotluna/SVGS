package ve.com.teeac.svgs.authentication.auth_twitter.data.data_source

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.data_source.convertFirebaseUserToUserInfo
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

class AuthenticationOAuthByFirebase(
    providerId: String
) {

    private val auth = FirebaseAuth.getInstance()

    private val provider = OAuthProvider.newBuilder(providerId)

    suspend fun signIn(activity: Activity): UserInfo? {
        val task = getTask(activity)
        val authResult = getPendingResult(task)
        return authResult?.let {
            convertFirebaseUserToUserInfo(authResult.user!!)
        }
    }

    private fun getTask(activity: Activity): Task<AuthResult?> {
        return auth.pendingAuthResult ?: auth.startActivityForSignInWithProvider(activity, provider.build())
    }

    private suspend fun getPendingResult(task: Task<AuthResult?>): AuthResult? {
        return try {
            task.await()
        } catch (e: Exception) {
            handleError(e)
            null
        }
    }

    private suspend fun handleError(e: Exception) {
        ExceptionManager.getInstance().setException(e.message!!)
    }
}

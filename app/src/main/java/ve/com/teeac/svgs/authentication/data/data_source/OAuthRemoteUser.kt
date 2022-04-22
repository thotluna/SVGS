package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.utils.convertFirebaseUserToUserInfo

abstract class OAuthRemoteUser(
    private val auth: FirebaseAuth,
    private val provider: OAuthProvider
) {
    suspend fun signIn(activity: Activity): User? {

        val result = getTask(activity)
        return result.let {
            it.user?.convertFirebaseUserToUserInfo()
        }
    }

    private suspend fun getTask(activity: Activity): AuthResult {

        return auth.pendingAuthResult?.await()
            ?: auth.startActivityForSignInWithProvider(activity, provider).await()
    }
}

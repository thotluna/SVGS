package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.android.gms.tasks.Task
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

        val task = getTask(activity).await()
        return task?.let {
            it.user?.convertFirebaseUserToUserInfo()
        }
    }

    private fun getTask(activity: Activity): Task<AuthResult?> {

        return auth.pendingAuthResult ?: auth.startActivityForSignInWithProvider(
            activity,
            provider
        )
    }
}

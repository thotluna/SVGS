package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.di.AuthTwitter
import javax.inject.Inject

class AuthenticationOAuthByFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    @AuthTwitter private val provider: OAuthProvider
) {

    suspend fun signIn(activity: Activity): UserInfo? {

        val task = getTask(activity).await()
        return task?.let {
            convertFirebaseUserToUserInfo(task.user!!)
        }
    }

    private fun getTask(activity: Activity): Task<AuthResult?> {

        return auth.pendingAuthResult ?: auth.startActivityForSignInWithProvider(
            activity,
            provider
        )
    }
}

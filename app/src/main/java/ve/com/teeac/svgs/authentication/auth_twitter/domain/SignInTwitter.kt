package ve.com.teeac.svgs.authentication.auth_twitter.domain

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.auth_twitter.data.data_source.AuthenticationOAuthByFirebase
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

class SignInTwitter(
    providerId: String
) {

    private val auth = AuthenticationOAuthByFirebase(providerId)

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    fun signIn(activity: Activity): UserInfo? {
        var result: UserInfo? = null
        scope.launch {
            result = try {
                auth.signIn(activity = activity)
            } catch (e: Exception) {
                handleError(e)
                null
            }
        }
        return result
    }

    private fun handleError(e: Exception) {
        scope.launch {
            ExceptionManager.getInstance().setException(e.message!!)
        }
    }
}

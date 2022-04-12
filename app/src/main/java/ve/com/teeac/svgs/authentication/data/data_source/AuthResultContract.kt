package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import timber.log.Timber
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException

class AuthResultContract : ActivityResultContract<Int, Credentials?>() {

    override fun parseResult(resultCode: Int, intent: Intent?): Credentials? {
        return when (resultCode) {
            Activity.RESULT_OK -> getCredentials(GoogleSignIn.getSignedInAccountFromIntent(intent))
            else -> null
        }
    }

    override fun createIntent(context: Context, input: Int): Intent {
        return getGoogleSignInClient(context).signInIntent.putExtra("input", input)
    }

    private fun getCredentials(task: Task<GoogleSignInAccount>?): Credentials? {
        return try {
            val account = task?.getResult(ApiException::class.java) ?: throw CredentialsFailException("User does not exit")
            if (account.idToken != null) Credentials(account.idToken!!, null) else null
        } catch (e: ApiException) {
            Timber.d(e.message)
            null
        }
    }

    private fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_app_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }
}

data class Credentials(val idToken: String, val accessToken: String?)

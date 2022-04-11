package ve.com.teeac.svgs.authentication.auth_twitter.domain

import android.app.Activity
import ve.com.teeac.svgs.authentication.auth_twitter.data.data_source.AuthenticationOAuthByFirebase
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.di.AuthTwitter
import javax.inject.Inject

class SignInTwitterUseCase @Inject constructor(
    @AuthTwitter private var auth: AuthenticationOAuthByFirebase
) {

    suspend operator fun invoke(activity: Activity): UserInfo? {
        return auth.signIn(activity = activity)
    }
}

package ve.com.teeac.svgs.authentication.domain.use_case

import android.app.Activity
import ve.com.teeac.svgs.authentication.data.data_source.AuthenticationOAuthByFirebase
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

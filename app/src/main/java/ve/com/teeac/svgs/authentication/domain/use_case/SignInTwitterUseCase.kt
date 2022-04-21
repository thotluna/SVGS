package ve.com.teeac.svgs.authentication.domain.use_case

import android.app.Activity
import ve.com.teeac.svgs.authentication.data.data_source.AuthenticationOAuthByFirebase
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.di.AuthTwitter
import javax.inject.Inject

class SignInTwitterUseCase @Inject constructor(
    @AuthTwitter private var auth: AuthenticationOAuthByFirebase
) {

    suspend operator fun invoke(activity: Activity): User? {
        return auth.signIn(activity = activity)
    }
}

package ve.com.teeac.svgs.authentication.domain.use_case

import android.app.Activity
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository
import javax.inject.Inject

class SignInTwitterUseCase @Inject constructor(
    private val repository: OAuthRepository
) {

    suspend operator fun invoke(activity: Activity): User? {
        return repository.signIn(activity = activity)
    }
}

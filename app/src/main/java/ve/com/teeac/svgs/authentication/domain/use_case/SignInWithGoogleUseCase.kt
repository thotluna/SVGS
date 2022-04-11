package ve.com.teeac.svgs.authentication.domain.use_case

import ve.com.teeac.svgs.authentication.data.data_source.Credentials
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(credentials: Credentials): UserInfo? {
        return repository.signInWithGoogle(credentials)
    }
}

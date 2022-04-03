package ve.com.teeac.svgs.authentication.auth_google.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(task: Task<GoogleSignInAccount>?) {
        repository.signInWithGoogle(task)
    }
}

package ve.com.teeac.svgs.authentication.domain.use_case

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

@ViewModelScoped
class SignInByEmailAndPasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend operator fun invoke(email: String, password: String): User? {
        return withContext(ioDispatcher) {
            return@withContext repository.signInByEmailAndPassword(email, password)
        }
    }
}

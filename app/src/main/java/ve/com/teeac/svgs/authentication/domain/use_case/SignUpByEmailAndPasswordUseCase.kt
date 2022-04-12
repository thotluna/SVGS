package ve.com.teeac.svgs.authentication.domain.use_case

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class SignUpByEmailAndPasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(email: String, password: String): UserInfo? {
        return withContext(ioDispatcher) {
            return@withContext repository.signUpByEmailAndPassword(email, password)
        }
    }
}

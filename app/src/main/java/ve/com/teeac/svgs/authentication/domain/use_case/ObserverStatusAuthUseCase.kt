package ve.com.teeac.svgs.authentication.domain.use_case

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class ObserverStatusAuthUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<User?> = repository.authStateChanges()
}

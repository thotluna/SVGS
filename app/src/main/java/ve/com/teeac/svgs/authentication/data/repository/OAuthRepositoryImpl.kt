package ve.com.teeac.svgs.authentication.data.repository

import android.app.Activity
import ve.com.teeac.svgs.authentication.data.data_source.OAuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository
import javax.inject.Inject

class OAuthRepositoryImpl @Inject constructor(
    private val service: OAuthRemoteUser
) : OAuthRepository {
    override suspend fun signIn(activity: Activity): User? {
        return service.signIn(activity)
    }
}

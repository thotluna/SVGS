package ve.com.teeac.svgs.authentication.domain.repositories

import android.app.Activity
import ve.com.teeac.svgs.authentication.data.models.User

interface OAuthRepository {
    suspend fun signIn(activity: Activity): User?
}

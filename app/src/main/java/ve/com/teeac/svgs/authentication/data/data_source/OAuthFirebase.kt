package ve.com.teeac.svgs.authentication.data.data_source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import ve.com.teeac.svgs.authentication.di.AuthTwitter
import javax.inject.Inject

class OAuthFirebase @Inject constructor(
    auth: FirebaseAuth,
    @AuthTwitter private val provider: OAuthProvider
) : OAuthRemoteUser(auth, provider)

package ve.com.teeac.svgs.authentication.auth_google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ve.com.teeac.svgs.authentication.auth_google.domain.SignInWithGoogleUseCase
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    private val useCase: SignInWithGoogleUseCase
) : ViewModel() {

    fun singInGoogle(credentials: Credentials?) {
        viewModelScope.launch {
            credentials?.idToken?.let {
                withContext(Dispatchers.IO) {
                    useCase(credentials)
                }
            } ?: throw CredentialsFailException("User does not exist")
        }
    }
}

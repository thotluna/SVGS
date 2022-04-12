package ve.com.teeac.svgs.authentication.presentation.google_button

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ve.com.teeac.svgs.authentication.data.data_source.Credentials
import ve.com.teeac.svgs.authentication.domain.use_case.SignInWithGoogleUseCase
import ve.com.teeac.svgs.core.exceptions.ExceptionManager
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
            } ?: ExceptionManager.getInstance().setException("User does not exist")
        }
    }
}
package ve.com.teeac.svgs.authentication.auth_google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.auth_google.domain.SignInWithGoogleUseCase
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    private val useCase: SignInWithGoogleUseCase
) : ViewModel() {

    fun singInGoogle(task: Task<GoogleSignInAccount>?) {
        viewModelScope.launch {
            useCase(task)
        }
    }
}

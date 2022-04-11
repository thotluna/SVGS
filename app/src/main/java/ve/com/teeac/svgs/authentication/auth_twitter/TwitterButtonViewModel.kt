package ve.com.teeac.svgs.authentication.auth_twitter

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.auth_twitter.domain.SignInTwitterUseCase
import ve.com.teeac.svgs.core.exceptions.ExceptionManager
import javax.inject.Inject

@HiltViewModel
class TwitterButtonViewModel @Inject constructor(
    private val useCase: SignInTwitterUseCase
) : ViewModel() {

    fun signIn(activity: Activity) {
        viewModelScope.launch {
            try {
                useCase(activity)
            } catch (e: Exception) {
                ExceptionManager.getInstance().setException(e.message!!)
            }
        }
    }
}

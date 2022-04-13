package ve.com.teeac.svgs.authentication.presentation.twitter_button

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.domain.use_case.OAuthUseCase
import ve.com.teeac.svgs.core.exceptions.ExceptionManager
import javax.inject.Inject

@HiltViewModel
class TwitterButtonViewModel @Inject constructor(
    private val useCase: OAuthUseCase
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

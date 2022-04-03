package ve.com.teeac.svgs.authentication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import ve.com.teeac.svgs.authentication.domain.use_case.ObserverStatusAuthUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val signUpUseCase: SignUpByEmailAndPasswordUseCase,
    private val signInUseCase: SignInByEmailAndPasswordUseCase,
    private val observerStatusAuthUseCase: ObserverStatusAuthUseCase
) : ViewModel() {

    private var _isSingIn = mutableStateOf(true)
    val isSingIn: State<Boolean> = _isSingIn

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            observerStatusAuthUseCase().collectLatest {
                it?.let {
                    _isLoading.value = false
                }
            }
        }
    }

    fun onEvent(event: SingEvent) {
        when (event) {
            is SingEvent.ChangeSing -> onChangeSing()
            is SingEvent.Sing -> onSing(event.username, event.password)
        }
    }

    private fun onSing(username: String, password: String) {
        _isLoading.value = !isLoading.value
        when (isSingIn.value) {
            true -> onSingIn(username, password)
            false -> onSingUp(username, password)
        }
    }

    private fun onSingUp(username: String, password: String) {
        Timber.d("username: $username, password: $password")
        viewModelScope.launch {
            try {
                signUpUseCase(username, password)
            } catch (e: AuthenticationException) {
                Timber.d(e)
            }
        }
    }

    private fun onSingIn(username: String, password: String) {
        Timber.d("username: $username, password: $password")
        viewModelScope.launch {
            try {
                signInUseCase(username, password)
            } catch (e: AuthenticationException) {
                Timber.d(e)
            }
        }
    }

    private fun onChangeSing() {
        _isSingIn.value = !isSingIn.value
    }
}

package ve.com.teeac.svgs.authentication.presentation.form

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase
import javax.inject.Inject

@HiltViewModel
class SignFormViewModel @Inject constructor(
    private val signUpUseCase: SignUpByEmailAndPasswordUseCase,
    private val signInUseCase: SignInByEmailAndPasswordUseCase
) : ViewModel() {

    private val _state = mutableStateOf(SignFormState())
    val state: State<SignFormState> = _state

    fun onEvent(event: SignFormEvent) {
        when (event) {
            is SignFormEvent.ChangedIsValidate -> _state.value = state.value.copy(
                isValidation = !state.value.isValidation
            )
            is SignFormEvent.ChangedUsername -> _state.value = state.value.copy(
                username = event.username
            )
            is SignFormEvent.ChangedPassword -> _state.value = state.value.copy(
                password = event.password
            )
            is SignFormEvent.ChangedPasswordVisibility -> _state.value = state.value.copy(
                passwordVisible = !state.value.passwordVisible
            )
            is SignFormEvent.ChangedConfirmPassword -> _state.value = state.value.copy(
                confirmPassword = event.password
            )
            is SignFormEvent.ChangedConfirmPasswordVisibility -> _state.value = state.value.copy(
                confirmPasswordVisible = !state.value.confirmPasswordVisible
            )
            is SignFormEvent.ToggleForm -> _state.value = state.value.copy(
                isSignIn = !state.value.isSignIn
            )
            is SignFormEvent.Submit -> sign(state.value.username, state.value.password)
        }
    }

    private fun sign(username: String, password: String) {
        viewModelScope.launch {
            if (state.value.isSignIn) {
                signInUseCase(username, password)
            } else {
                signUpUseCase(username, password)
            }
        }
    }
}

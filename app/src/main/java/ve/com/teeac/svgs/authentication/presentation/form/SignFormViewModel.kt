package ve.com.teeac.svgs.authentication.presentation.form

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SignFormViewModel @Inject constructor() : ViewModel() {

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
        }
    }
}

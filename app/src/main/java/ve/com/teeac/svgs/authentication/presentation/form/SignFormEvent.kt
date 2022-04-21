package ve.com.teeac.svgs.authentication.presentation.form

sealed class SignFormEvent {
    object ChangedIsValidate : SignFormEvent()
    data class ChangedUsername(val username: String) : SignFormEvent()
    data class ChangedPassword(val password: String) : SignFormEvent()
    object ChangedPasswordVisibility : SignFormEvent()
    data class ChangedConfirmPassword(val password: String) : SignFormEvent()
    object ChangedConfirmPasswordVisibility : SignFormEvent()
    object ToggleForm : SignFormEvent()
    object Submit : SignFormEvent()
}

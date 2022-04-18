package ve.com.teeac.svgs.authentication.presentation.form

import ve.com.teeac.svgs.authentication.domain.ValidationField

data class SignFormState(
    val isValidation: Boolean = false,
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val isSignIn: Boolean = true,
) {
    val usernameErrors =
        ValidationField(username).notBlank().emailValid().result()
    val passwordErrors =
        ValidationField(password).notBlank().passwordValid().result()
    val confirmPasswordErrors =
        ValidationField(confirmPassword).notBlank().compareTo(password).result()

    val hasError = usernameErrors.isNotEmpty() ||
        passwordErrors.isNotEmpty() ||
        confirmPasswordErrors.isNotEmpty()
}

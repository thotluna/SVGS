package ve.com.teeac.svgs.authentication

import androidx.core.util.PatternsCompat

class ValidationField(private val value: String) {

    private val list = mutableSetOf<String>()

    private val patternPassword = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,32}$")

    companion object {
        const val BLANK = "Field cannot be empty."
        const val NOT_EMAIL = "Field must be a valid email."
        const val PASSWORD_INVALID = "Password it's not valid"
        const val PASSWORD_NOT_MATCH = "Password confirm does not match password"
    }

    fun notBlank(): ValidationField {
        if (value.isBlank()) {
            list.add(BLANK)
        } else {
            list.remove(BLANK)
        }

        return this
    }

    fun emailValid(): ValidationField {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()) {
            list.add(NOT_EMAIL)
        } else {
            list.remove(NOT_EMAIL)
        }

        return this
    }

    fun passwordValid(): ValidationField {
//        println("Validacion ${ patternPassword.matches(value) }")
        if (!patternPassword.matches(value)) {
            list.add(PASSWORD_INVALID)
        } else {
            list.remove(PASSWORD_INVALID)
        }

        return this
    }

    fun compareTo(otherValue: String): ValidationField {
        if (!value.contentEquals(otherValue)) {
            list.add(PASSWORD_NOT_MATCH)
        } else {
            list.remove(PASSWORD_NOT_MATCH)
        }
        return this
    }

    fun result(): List<String> = list.toList()
}

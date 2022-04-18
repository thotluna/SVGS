package ve.com.teeac.svgs.authentication.presentation.form

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLog
import ve.com.teeac.svgs.authentication.domain.ValidationField

@ExperimentalComposeUiApi
@RunWith(RobolectricTestRunner::class)

@LooperMode(LooperMode.Mode.PAUSED)
class SignFormTest {

    @get: Rule
    val compose = createComposeRule()

    private val username = "username@email.com"
    private val password = "1aA#2345"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    private fun setComposeGeneric() {
        compose.setContent {
            SignForm(onSubmit = { _, _ -> })
        }
    }

    @Test
    fun `Should be swith form Sign In and Sign Up`() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .assertIsDisplayed()
            .performClick()

        compose.onNodeWithText("You are already a user? Sing In")
            .assertIsDisplayed()
            .performClick()

        compose.onNodeWithText("New here? Sing Up")
            .assertIsDisplayed()
    }

    @Test
    fun `should be return form Sign In`() {

        setComposeGeneric()

        compose.onRoot().printToLog("TAG")

        compose.onNodeWithText("Username")
            .assertIsDisplayed()
        compose.onNodeWithText("Password")
            .assertIsDisplayed()
        compose.onNodeWithText("Sign In")
            .assertIsDisplayed()
        compose.onNodeWithText("New here? Sing Up")
            .assertIsDisplayed()
    }

    @Test
    fun `should be return form Sign up`() {

        setComposeGeneric()

        compose.onNode(hasText("You are already a user? Sing In"))
            .assertDoesNotExist()

        compose.onNodeWithText("New here? Sing Up")
            .assertIsDisplayed()
            .performClick()

        compose.onNodeWithText("Username")
            .assertIsDisplayed()
        compose.onNodeWithText("Password")
            .assertIsDisplayed()
        compose.onNodeWithText("Confirm Password")
            .assertIsDisplayed()
        compose.onNodeWithText("Sign Up")
            .assertIsDisplayed()
        compose.onNodeWithText("You are already a user? Sing In")
            .assertIsDisplayed()

        compose.onNodeWithText("You are already a user? Sing In")
            .assertIsDisplayed()
            .performClick()

        compose.onNodeWithText("New here? Sing Up")
            .assertIsDisplayed()

        compose.onNode(hasText("Confirm Password"))
            .assertDoesNotExist()

        compose.onNode(hasText("You are already a user? Sing In"))
            .assertDoesNotExist()
    }

    @Test
    fun `showld be changed focus the Username field a Password field`() {

        setComposeGeneric()

        compose.onNodeWithText("Username")
            .assertIsDisplayed()

        compose.onNodeWithText("Username")
            .assert(hasImeAction(ImeAction.Next))
            .performImeAction()

        compose.onNodeWithText("Password")
            .assertIsFocused()
    }

    @Test
    fun `showld be show list to error from password`() {

        setComposeGeneric()

        compose.onNodeWithText("Password")
            .assertIsDisplayed()

        compose.onNodeWithText("Password")
            .assert(hasImeAction(ImeAction.Done))
            .performImeAction()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_USERNAME)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_USERNAME}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(2)
            .also {
                it.onFirst().assert(hasText(ValidationField.BLANK))
                it.onLast().assert(hasText(ValidationField.NOT_EMAIL))
            }
    }

    @Test
    fun `showld be changed focus the password to confirm password `() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Password")
            .assertIsDisplayed()

        compose.onNodeWithText("Password")
            .assert(hasImeAction(ImeAction.Next))
            .performImeAction()

        compose.onNodeWithText("Confirm Password")
            .assertIsFocused()
    }

    @Test
    fun `showld be show list to error from confirm password`() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Confirm Password")
            .assertIsDisplayed()

        compose.onNodeWithText("Confirm Password")
            .assert(hasImeAction(ImeAction.Done))
            .performImeAction()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD_CONFIRM)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD_CONFIRM}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(1)
            .also {
                it.onFirst().assert(hasText(ValidationField.BLANK))
            }
    }

    @Test
    fun `showld be show and hide value password`() {

        setComposeGeneric()

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNode(hasText(password))
            .assertDoesNotExist()

        compose.onNodeWithText("Password")
            .onChildren()
            .filter(hasContentDescription("Visible Password"))
            .onFirst()
            .performClick()

        compose.onNodeWithText(password)
            .assertIsDisplayed()

        compose.onNodeWithText("Password")
            .onChildren()
            .filter(hasContentDescription("Hide Password"))
            .onFirst()
            .performClick()

        compose.onNode(hasText(password))
            .assertDoesNotExist()
    }

    @Test
    fun `showld be show and hide value confirm password`() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Confirm Password")
            .performTextInput(password)

        compose.onNode(hasText(password))
            .assertDoesNotExist()

        compose.onNodeWithText("Confirm Password")
            .onChildren()
            .filter(hasContentDescription("Visible Password"))
            .onFirst()
            .performClick()

        compose.onNodeWithText(password)
            .assertIsDisplayed()

        compose.onNodeWithText("Confirm Password")
            .onChildren()
            .filter(hasContentDescription("Hide Password"))
            .onFirst()
            .performClick()

        compose.onNode(hasText(password))
            .assertDoesNotExist()
    }

    @Test
    fun `should be all desable`() {
        compose.setContent {
            SignForm(
                onSubmit = { _, _ -> },
                enabled = false
            )
        }

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Username")
            .assertIsNotEnabled()

        compose.onNodeWithText("Password")
            .assertIsNotEnabled()

        compose.onNodeWithText("Confirm Password")
            .assertIsNotEnabled()

        compose.onNodeWithText("Sign Up")
            .assertIsNotEnabled()
    }

    @Test
    fun `should be show list error with Black and Email invalid in Username field`() {

        setComposeGeneric()

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_USERNAME)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_USERNAME}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(2)
            .also {
                it.onFirst().assert(hasText(ValidationField.BLANK))
                it.onLast().assert(hasText(ValidationField.NOT_EMAIL))
            }
    }

    @Test
    fun `should be show list error with only Email invalid in Username field`() {

        setComposeGeneric()

        compose.onNodeWithText("Username")
            .performTextInput("test")

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_USERNAME)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_USERNAME}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(1)
            .also {
                it.onFirst().assert(hasText(ValidationField.NOT_EMAIL))
            }
    }

    @Test
    fun `should be show without list error in Username field`() {

        setComposeGeneric()

        compose.onNodeWithText("Username")
            .performTextInput(username)

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_USERNAME)
            .onChildren()
            .assertCountEquals(1)
            .filter(hasTestTag("${TestTags.SIGN_FIELD_USERNAME}_Erros"))
            .assertCountEquals(0)
    }

    @Test
    fun `should be show list Black and Not Valid error in Password field`() {

        setComposeGeneric()

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(2)
            .also {
                it.onFirst().assert(hasText(ValidationField.BLANK))
                it.onLast().assert(hasText(ValidationField.PASSWORD_INVALID))
            }
    }

    @Test
    fun `should be show list error only Not Valid in Password field`() {

        setComposeGeneric()

        compose.onNodeWithText("Password")
            .performTextInput("12345")

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(1)
            .also {
                it.onFirst().assert(hasText(ValidationField.PASSWORD_INVALID))
            }
    }

    @Test
    fun `should be show without list in Password field`() {

        setComposeGeneric()

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD)
            .onChildren()
            .assertCountEquals(1)
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD}_Erros"))
            .assertCountEquals(0)
    }

    @Test
    fun `should be show list error Black and Not Match in Confirm Password field `() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign Up")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD_CONFIRM)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD_CONFIRM}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(2)
            .also {
                it.onFirst().assert(hasText(ValidationField.BLANK))
                it.onLast().assert(hasText(ValidationField.PASSWORD_NOT_MATCH))
            }
    }

    @Test
    fun `should be show list error only Not Match in Confirm Password field`() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Confirm Password")
            .performTextInput("123")

        compose.onNodeWithText("Sign Up")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD_CONFIRM)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD_CONFIRM}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(1)
            .also {
                it.onFirst().assert(hasText(ValidationField.PASSWORD_NOT_MATCH))
            }
    }

    @Test
    fun `should be show list error Black and Not Match changed Password in Confirm Password field `() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Password")
            .performTextInput("12313")

        compose.onNodeWithText("Confirm Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign Up")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD_CONFIRM)
            .onChildren()
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD_CONFIRM}_Errors"))
            .onFirst()
            .onChildren()
            .assertCountEquals(1)
            .also {
                it.onFirst().assert(hasText(ValidationField.PASSWORD_NOT_MATCH))
            }
    }

    @Test
    fun `should be show not list error in Confirm Password field `() {

        setComposeGeneric()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Confirm Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign Up")
            .performClick()

        compose.onNodeWithTag(TestTags.SIGN_FIELD_PASSWORD_CONFIRM)
            .onChildren()
            .assertCountEquals(1)
            .filter(hasTestTag("${TestTags.SIGN_FIELD_PASSWORD_CONFIRM}_Erros"))
            .assertCountEquals(0)
    }

    @Test
    fun `should be return user and password correct`() {
        compose.setContent {
            SignForm(onSubmit = { user, pass ->
                assertEquals(username, user)
                assertEquals(password, pass)
            })
        }

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithText("Username")
            .performTextInput(username)

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign In")
            .performClick()

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Confirm Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign Up")
            .performClick()
    }
}

package ve.com.teeac.svgs.authentication.presentation.form

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.ValidationField
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
@ExperimentalComposeUiApi
// @RunWith(RobolectricTestRunner::class)
// @LooperMode(LooperMode.Mode.PAUSED)
class SignFormTest {

    @get:Rule(order = 1)
    val compose = createComposeRule()

    @MockK(relaxed = true)
    lateinit var inUseCase: SignInByEmailAndPasswordUseCase

    @MockK
    lateinit var upUseCase: SignUpByEmailAndPasswordUseCase

    private lateinit var viewModel: SignFormViewModel

    private val username = "username@email.com"
    private val password = "1aA#2345"

    @Before
    @Throws(Exception::class)
    fun setUp() {

        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = SignFormViewModel(upUseCase, inUseCase)

//        ShadowLog.stream = System.out
    }

    private fun setComposeGeneric() {
        compose.setContent {
            SignForm(
                onSubmit = { },
                viewModel = viewModel
            )
        }
    }

    @Test
    fun should_be_swith_form_Sign_In_and_Sign_Up() {

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
    fun should_be_return_form_Sign_In() {

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
    fun should_be_return_form_Sign_up() {

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
    fun showld_be_changed_focus_the_Username_field_a_Password_field() {

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
    fun showld_be_show_list_to_error_from_password() {

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
    fun showld_be_changed_focus_the_password_to_confirm_password() {

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
    fun showld_be_show_list_to_error_from_confirm_password() {

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
    fun showld_be_show_and_hide_value_password() {

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
    fun showld_be_show_and_hide_value_confirm_password() {

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
    fun should_be_all_desable() {
        compose.setContent {
            SignForm(
                onSubmit = { },
                enabled = false,
                viewModel = viewModel
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
    fun should_be_show_list_error_with_Black_and_Email_invalid_in_Username_field() {

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
    fun should_be_show_list_error_with_only_Email_invalid_in_Username_field() {

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
    fun should_be_show_without_list_error_in_Username_field() {

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
    fun should_be_show_list_Black_and_Not_Valid_error_in_Password_field() {

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
    fun should_be_show_list_error_only_Not_Valid_in_Password_field() {

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
    fun should_be_show_without_list_in_Password_field() {

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
    fun should_be_show_list_error_Black_and_Not_Match_in_Confirm_Password_field() {

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
    fun should_be_show_list_error_only_Not_Match_in_Confirm_Password_field() {

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
    fun should_be_show_list_error_Black_and_Not_Match_changed_Password_in_Confirm_Password_field() {

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
    fun should_be_show_not_list_error_in_Confirm_Password_field() {

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
    fun should_not_do_anything_when_I_click_sign_in_with_errors() {
        compose.setContent {
            SignForm(
                onSubmit = { },
                viewModel = viewModel
            )
        }

        val user = User(
            displayName = "test",
            email = username,
            token = "token",
        )

        coEvery { inUseCase(any(), any()) } returns user

        compose.onNodeWithText("Sign In")
            .performClick()

        coVerify(exactly = 0) { inUseCase(username, password) }
    }

    @Test
    fun should_not_do_anything_when_I_click_sign_up_with_errors() {
        compose.setContent {
            SignForm(
                onSubmit = { },
                viewModel = viewModel
            )
        }

        val user = User(
            displayName = "test",
            email = username,
            token = "token",
        )

        coEvery { upUseCase(any(), any()) } returns user

        compose.onNodeWithText("Sign In")
            .performClick()

        coVerify(exactly = 0) { upUseCase(username, password) }
    }

    @Test
    fun should_be_return_user_and_password_correct_in_sign_in() = runTest {
        compose.setContent {
            SignForm(
                onSubmit = { },
                viewModel = viewModel
            )
        }

        val user = User(
            displayName = "test",
            email = username,
            token = "token",
        )

        coEvery { inUseCase(any(), any()) } returns user

        compose.onNodeWithText("Username")
            .performTextInput(username)

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign In")
            .performClick()

        coVerify(exactly = 1) { inUseCase(username, password) }
        confirmVerified(inUseCase)
    }

    @Test
    fun should_be_return_user_and_password_correct_in_sign_up() = runTest {
        compose.setContent {
            SignForm(
                onSubmit = { },
                viewModel = viewModel
            )
        }

        val user = User(
            displayName = "test",
            email = username,
            token = "token",
        )

        coEvery { upUseCase(any(), any()) } returns user

        compose.onNodeWithText("New here? Sing Up")
            .performClick()

        compose.onNodeWithText("Username")
            .performTextInput(username)

        compose.onNodeWithText("Password")
            .performTextInput(password)

        compose.onNodeWithText("Confirm Password")
            .performTextInput(password)

        compose.onNodeWithText("Sign Up")
            .performClick()

        coVerify(exactly = 1) { upUseCase(username, password) }
        confirmVerified(upUseCase)
    }
}

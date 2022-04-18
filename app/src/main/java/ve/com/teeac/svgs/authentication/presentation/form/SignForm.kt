package ve.com.teeac.svgs.authentication.presentation.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.core.presentation.FieldText

@ExperimentalComposeUiApi
@Composable
fun SignForm(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSubmit: (username: String, password: String) -> Unit,
    viewModel: SignFormViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun handleOnSubmit() {
        viewModel.onEvent(SignFormEvent.ChangedIsValidate)

        if (state.hasError || !enabled) return

        keyboardController?.hide()
        viewModel.onEvent(SignFormEvent.ChangedIsValidate)
        onSubmit(state.username, state.password)
    }

    Column(
        modifier = modifier
    ) {
        FieldText(
            value = state.username,
            onValueChange = { viewModel.onEvent(SignFormEvent.ChangedUsername(it)) },
            label = stringResource(R.string.sign_username),
            enabled = enabled,
            testTag = TestTags.SIGN_FIELD_USERNAME,
            validate = state.isValidation,
            validation = state.usernameErrors,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        FieldText(
            value = state.password,
            onValueChange = { viewModel.onEvent(SignFormEvent.ChangedPassword(it)) },
            label = stringResource(R.string.sign_password),
            testTag = TestTags.SIGN_FIELD_PASSWORD,
            enabled = enabled,
            validate = state.isValidation,
            validation = state.passwordErrors,
            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.onEvent(SignFormEvent.ChangedPasswordVisibility) }) {
                    /*TODO: changed icon */
                    Icon(
                        imageVector = if (state.passwordVisible) Icons.Default.ArrowDropDown else Icons.Default.ArrowForward,
                        contentDescription = if (state.passwordVisible) "Hide Password" else "Visible Password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = if (state.isSignIn) ImeAction.Done else ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                },
                onDone = {
                    handleOnSubmit()
                }
            )
        )
        if (!state.isSignIn) {
            FieldText(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(SignFormEvent.ChangedConfirmPassword(it)) },
                label = stringResource(R.string.sign_confirm_password),
                testTag = TestTags.SIGN_FIELD_PASSWORD_CONFIRM,
                enabled = enabled,
                validate = state.isValidation,
                validation = state.confirmPasswordErrors,
                visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(SignFormEvent.ChangedConfirmPasswordVisibility) }) {
                        /*TODO: changed icon */
                        Icon(
                            imageVector = if (state.confirmPasswordVisible) Icons.Default.ArrowDropDown else Icons.Default.ArrowForward,
                            contentDescription = if (state.confirmPasswordVisible) "Hide Password" else "Visible Password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleOnSubmit()
                    }
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier) {
            SubmitButton(
                onSubmit = { handleOnSubmit() },
                isSingIn = state.isSignIn,
                enable = enabled
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (state.isSignIn) stringResource(R.string.move_to_sing_up)
            else stringResource(R.string.move_to_sing_in),
            modifier = Modifier
                .clickable { viewModel.onEvent(SignFormEvent.ToggleForm) }
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
    enable: Boolean = true,
    isSingIn: Boolean = true
) {
    Button(
        onClick = { onSubmit() },
        shape = RoundedCornerShape(36.dp),
        enabled = enable,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.SIGN_IN_BUTTON)
    ) {
        Text(
            text = if (isSingIn) stringResource(R.string.sing_in)
            else stringResource(id = R.string.sing_up),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }
}

object TestTags {
    const val SIGN_FIELD_USERNAME = "TAG_FIELD_USERNAME"
    const val SIGN_FIELD_PASSWORD = "TAG_FIELD_PASSWORD"
    const val SIGN_FIELD_PASSWORD_CONFIRM = "TAG_FIELD_PASSWORD_CONFIRM"
    const val SIGN_IN_BUTTON = "Sign in Button"
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun SignFormPreview() {
    SignForm(
        onSubmit = { _, _ -> }
    )
}

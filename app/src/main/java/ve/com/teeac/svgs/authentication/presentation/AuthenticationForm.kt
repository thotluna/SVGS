package ve.com.teeac.svgs.authentication.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.authentication.ValidationField
import ve.com.teeac.svgs.authentication.auth_google.GoogleButton
import ve.com.teeac.svgs.authentication.auth_twitter.TwitterButton
import ve.com.teeac.svgs.core.presentation.MyField

@ExperimentalComposeUiApi
@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    viewModel: SignViewModel = hiltViewModel(),
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by rememberSaveable { mutableStateOf("") }
    val usernameError = ValidationField(username)
        .notBlank()
        .emailValid()
        .result().toMutableStateList()

    var password by rememberSaveable { mutableStateOf("") }
    val passwordError = ValidationField(password)
        .notBlank()
        .passwordValid()
        .result().toMutableStateList()

    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val confirmPasswordError = ValidationField(confirmPassword)
        .notBlank()
        .compareTo(password)
        .result().toMutableStateList()

    var validate by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    fun onSignInValidation() {
        validate = true

        if (!viewModel.isSingIn.value && confirmPasswordError.size > 0) return
        if (usernameError.size > 0 || passwordError.size > 0) return
        viewModel.onEvent(SingEvent.Sing(username, password))
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .scrollable(scrollState, orientation = Orientation.Vertical)
            ) {
                TitleForm()
                MyField(
                    value = username,
                    onValueChange = { username = it },
                    validation = ValidationField(username).notBlank().emailValid().result()
                        .toMutableStateList(),
                    validate = validate,
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MyField(
                    value = password,
                    onValueChange = { password = it },
                    validation = ValidationField(password).notBlank().passwordValid().result()
                        .toMutableStateList(),
                    validate = validate,
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = if (viewModel.isSingIn.value) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        },
                        onDone = {
                            onSignInValidation()
                            keyboardController?.hide()
                        }
                    ),
                )
                Spacer(modifier = Modifier.padding(8.dp))
                if (!viewModel.isSingIn.value)
                    MyField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        validation = ValidationField(confirmPassword).notBlank().passwordValid()
                            .compareTo(password).result().toMutableStateList(),
                        validate = validate,
                        label = { Text("Confirm Password") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onSignInValidation()
                            keyboardController?.hide()
                        }),
                    )
                Spacer(modifier = Modifier.padding(12.dp))

                SubmitButton(
                    isSingIn = viewModel.isSingIn.value,
                    isDisable = viewModel.isLoading.value
                ) {
                    onSignInValidation()
                    keyboardController?.hide()
                }

                SocialButtons(
                    isVisible = viewModel.isSingIn.value,
                    isDisable = viewModel.isLoading.value,
                    onLoading = {
                        viewModel.onEvent(
                            SingEvent.OnLoading
                        )
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))

                FormsSwitch(
                    isSingIn = viewModel.isSingIn.value,
                    onChangeForm = { viewModel.onEvent(it) }
                )
            }
        }
    }
}

@Composable
fun TitleForm() {
    Text(
        text = "Welcome",
        style = MaterialTheme.typography.h3,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    )
}

@Composable
private fun SubmitButton(
    isSingIn: Boolean,
    isDisable: Boolean,
    onSingInValidation: () -> Unit,
) {
    Button(
        onClick = { onSingInValidation() },
        shape = RoundedCornerShape(36.dp),
        enabled = !isDisable,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (isSingIn) stringResource(R.string.sing_in)
            else stringResource(id = R.string.sing_up),
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
    }

    if (isSingIn) {
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = stringResource(R.string.forgot_password),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /*TODO*/ },
        )
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun FormsSwitch(
    isSingIn: Boolean,
    onChangeForm: (SingEvent) -> Unit
) {
    Text(
        text = if (isSingIn) stringResource(R.string.move_to_sing_up)
        else stringResource(R.string.move_to_sing_in),
        modifier = Modifier
            .clickable { onChangeForm(SingEvent.ChangeSing) }
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SocialButtons(
    isVisible: Boolean,
    isDisable: Boolean,
    onLoading: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            GoogleButton(onLoading = onLoading, isDisable = isDisable)
            Text(text = "Or")
            TwitterButton(onLoading = onLoading, isDisable = isDisable)
        }
    }
}

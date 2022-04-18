package ve.com.teeac.svgs.authentication.presentation

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.authentication.presentation.form.SignForm
import ve.com.teeac.svgs.authentication.presentation.google_button.GoogleButton
import ve.com.teeac.svgs.authentication.presentation.twitter_button.TwitterButton

@ExperimentalComposeUiApi
@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    viewModel: SignViewModel = hiltViewModel(),
) {

    val scrollState = rememberScrollState()

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
                SignForm(
                    onSubmit = { username, password ->
                        viewModel.onEvent(SingEvent.Sing(username, password))
                    },
                    enabled = !viewModel.isLoading.value
                )

                SocialButtons(
                    isVisible = viewModel.isSingIn.value,
                    isDisable = viewModel.isLoading.value,
                    onLoading = {
                        viewModel.onEvent(
                            SingEvent.OnLoading
                        )
                    }
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

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.authentication.presentation.form.SignForm
import ve.com.teeac.svgs.authentication.presentation.google_button.GoogleButton
import ve.com.teeac.svgs.authentication.presentation.twitter_button.TwitterButton

@ExperimentalComposeUiApi
@Preview
@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    viewModel: SignViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()

    var isLoading = viewModel.isLoading.value

    fun handleSubmit() {
        viewModel.onEvent(SignEvent.OnLoading)
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
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
//                    .align(Alignment.TopCenter)
                    .scrollable(scrollState, orientation = Orientation.Vertical)
            ) {
                TitleForm()
                SignForm(
                    onSubmit = { handleSubmit() },
                    enabled = !isLoading
                )
                SocialButtons(
                    enabled = !isLoading,
                    onLoading = { isLoading = true },
                    modifier = Modifier.fillMaxWidth()
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
    onLoading: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        GoogleButton(onClick = onLoading, enabled = enabled)
        Text(text = "Or")
        TwitterButton(onLoading = onLoading, enabled = enabled)
    }
}

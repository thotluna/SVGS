package ve.com.teeac.svgs.authentication.presentation.status_user

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import ve.com.teeac.svgs.authentication.presentation.AuthenticationScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthenticationStatus(
    state: Boolean,
    content: @Composable () -> Unit
) {
    if (state) {
        content()
    } else {
        AuthenticationScreen()
    }
}

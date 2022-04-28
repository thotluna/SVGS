package ve.com.teeac.svgs.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import ve.com.teeac.svgs.authentication.presentation.status_user.AuthenticationStatus
import ve.com.teeac.svgs.authentication.presentation.status_user.authenticationState
import ve.com.teeac.svgs.navigation.AppGraph

@ExperimentalComposeUiApi
@Composable
fun MainScreen() {
    val authenticationState by authenticationState()
    AuthenticationStatus(
        state = authenticationState
    ) {
        AppScaffold {
            AppGraph(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}

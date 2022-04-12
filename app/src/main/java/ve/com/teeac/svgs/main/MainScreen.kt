package ve.com.teeac.svgs.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.presentation.AuthenticationScreen
import ve.com.teeac.svgs.navigation.AppGraph

@ExperimentalComposeUiApi
@Composable
fun MainScreen() {
    MainContainer(modifier = Modifier.fillMaxSize())
}

@ExperimentalComposeUiApi
@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    AppScaffold(
        modifier = modifier,
        content = {
            Content(
                modifier = Modifier.padding(it),
                userAuthenticated = viewModel.currentUser.collectAsState(null).value
            )
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun Content(
    modifier: Modifier = Modifier,
    userAuthenticated: UserInfo?
) {
    when (userAuthenticated) {
        null -> AuthenticationScreen()
        else -> AppGraph(modifier = modifier)
    }
}

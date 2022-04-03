package ve.com.teeac.svgs.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.presentation.AuthenticationScreen
import ve.com.teeac.svgs.authentication.presentation.SignOutViewModel
import ve.com.teeac.svgs.navigation.AppGraph

@Composable
fun MainScreen() {
    MainContainer(modifier = Modifier.fillMaxSize())
}

@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        floatingActionButton = { FAB() },
        content = {
            Content(
                modifier = Modifier.padding(it),
                userAuthenticated = viewModel.currentUser.collectAsState(null).value
            )
        }
    )
}

@Composable
fun FAB(viewModel: SignOutViewModel = hiltViewModel()) {
    FloatingActionButton(
        onClick = { viewModel.signOut() }
    ) {
        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
    }
}

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

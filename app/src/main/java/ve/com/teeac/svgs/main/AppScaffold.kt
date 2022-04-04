package ve.com.teeac.svgs.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import ve.com.teeac.svgs.authentication.presentation.SignOutViewModel
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val exceptionManager = remember { ExceptionManager.getInstance() }

    LaunchedEffect(key1 = 1) {
        exceptionManager.exceptionFlow.collectLatest {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        floatingActionButton = { FAB() },
    ) {
        content(it)
    }
}

@Composable
fun FAB(viewModel: SignOutViewModel = hiltViewModel()) {
    FloatingActionButton(
        onClick = { viewModel.signOut() }
    ) {
        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
    }
}

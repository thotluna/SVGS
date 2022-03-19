package ve.com.teeac.svgs.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ve.com.teeac.svgs.authentication.AuthenticationScreen

@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        AuthenticationScreen(modifier = Modifier.padding(it))
    }
}

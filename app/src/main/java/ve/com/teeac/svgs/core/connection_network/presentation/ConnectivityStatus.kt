package ve.com.teeac.svgs.core.connection_network.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ve.com.teeac.svgs.core.connection_network.models.ConnectionState

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus(
    content: @Composable () -> Unit
) {
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available

    if (isConnected) {
        content()
    } else {
        NoConnectionScreen()
    }
}

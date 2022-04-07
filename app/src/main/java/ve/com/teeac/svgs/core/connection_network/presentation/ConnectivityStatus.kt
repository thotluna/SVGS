package ve.com.teeac.svgs.core.connection_network.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ve.com.teeac.svgs.core.connection_network.models.ConnectionState
import ve.com.teeac.svgs.main.MainScreen

@ExperimentalCoroutinesApi
@Composable
fun ConnectivityStatus() {
    val connection by connectivityState()

    val isConnected = connection === ConnectionState.Available

    if (isConnected) {
        MainScreen()
    } else {
        NoConnectionScreen()
    }
}
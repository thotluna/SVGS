package ve.com.teeac.svgs.core.connection_network.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ve.com.teeac.svgs.core.connection_network.models.ConnectionState
import ve.com.teeac.svgs.core.connection_network.services.currentConnectivityState
import ve.com.teeac.svgs.core.connection_network.services.observeConnectivityAsFlow

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

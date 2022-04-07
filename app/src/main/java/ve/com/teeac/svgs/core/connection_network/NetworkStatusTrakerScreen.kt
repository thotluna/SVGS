package ve.com.teeac.svgs.core.connection_network

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import timber.log.Timber
import ve.com.teeac.svgs.core.connection_network.presentation.NoConnectionScreen

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun NetworkStatusTrackerScreen(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val networkStatusTracker = NetworkStatusTracker(context)
    val statusNetwork = networkStatusTracker.networkStatus
        .map(
            onUnavailable = { false },
            onAvailable = { true },
        )
        .collectAsState(initial = networkStatusTracker.hasConnection())

    Timber.d(" Cargndo: ${statusNetwork.value}")
    when (statusNetwork.value) {
        true -> content()
        false -> NoConnectionScreen()
    }
}

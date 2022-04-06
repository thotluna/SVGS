package ve.com.teeac.svgs.core.traker_connection

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.map
import timber.log.Timber

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

@Composable
fun EmptyScreen() {
}

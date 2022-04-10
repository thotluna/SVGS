package ve.com.teeac.svgs.core.connection_network.services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import ve.com.teeac.svgs.core.connection_network.models.ConnectionState

val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectionState {

        val network = connectivityManager.activeNetwork
        network ?: return ConnectionState.Unavailable
        val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return ConnectionState.Unavailable
         val connected = when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow(connectionStateTest: ConnectionState? = null) = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = if (connectionStateTest == null) networkCallback { connectionState -> trySend(connectionState) }
    else networkCallback { trySend(connectionStateTest) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

fun networkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}

package ve.com.teeac.svgs.core.connection_network

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

package ve.com.teeac.svgs.core.traker_connection

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

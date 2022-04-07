package ve.com.teeac.svgs.core.connection_network.models

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}

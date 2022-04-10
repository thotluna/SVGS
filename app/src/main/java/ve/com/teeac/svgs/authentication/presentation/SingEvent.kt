package ve.com.teeac.svgs.authentication.presentation

sealed class SingEvent {
    object ChangeSing : SingEvent()
    data class OnLoading(val loading: Boolean) : SingEvent()
    data class Sing(val username: String, val password: String) : SingEvent()
}

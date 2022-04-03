package ve.com.teeac.svgs.authentication.presentation

sealed class SingEvent {
    object ChangeSing : SingEvent()
    data class Sing(val username: String, val password: String) : SingEvent()
}

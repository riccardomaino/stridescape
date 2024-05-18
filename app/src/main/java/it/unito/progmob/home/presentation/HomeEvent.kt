package it.unito.progmob.home.presentation

sealed class HomeEvent {
    data object DismissPermissionDialog : HomeEvent()
    data class CheckPermissionResult(val permission : String, val isGranted : Boolean) : HomeEvent()
    data object StartRunningService : HomeEvent()
    data object StopRunningService : HomeEvent()
}
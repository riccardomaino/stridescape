package it.unito.progmob.home.presentation

sealed class HomeEvent {
    data object DismissPermissionDialog : HomeEvent()
    data class PermissionResult(val permission : String, val isGranted : Boolean) : HomeEvent()
}
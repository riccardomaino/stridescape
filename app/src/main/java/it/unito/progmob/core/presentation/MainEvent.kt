package it.unito.progmob.core.presentation

sealed class MainEvent {
    data object DismissPermissionDialog : MainEvent()
    data class PermissionResult(val permission : String, val isGranted : Boolean) : MainEvent()

}
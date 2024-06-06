package it.unito.progmob.main.presentation

sealed class MainEvent {
    data object DismissPermissionDialog : MainEvent()
    data class PermissionResult(val permission : String, val isGranted : Boolean) : MainEvent()
    data class ShowFloatingActionButton(val isShown : Boolean) : MainEvent()
}
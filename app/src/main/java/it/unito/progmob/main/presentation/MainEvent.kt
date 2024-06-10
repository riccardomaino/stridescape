package it.unito.progmob.main.presentation

/**
 * Sealed class representing events that can occur on the main screen.
 */
sealed class MainEvent {
    /**
     * Event to dismiss the permission dialog.
     */
    data object DismissPermissionDialog : MainEvent()
    /**
     * Event to handle the result of a permission request.
     *
     * @param permission The permission that was requested.
     * @param isGranted Whether the permission was granted or not.
     */
    data class PermissionResult(val permission : String, val isGranted : Boolean) : MainEvent()
    /**
     * Event to show or hide the floating action button.
     *
     * @param isShown Whether the floating action button should be shown or not.
     */
    data class ShowFloatingActionButton(val isShown : Boolean) : MainEvent()
}
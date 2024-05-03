package it.unito.progmob.home.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import it.unito.progmob.home.presentation.HomeEvent

class HomeViewModel: ViewModel() {

    // Queue used to show some potential different permission dialog
    private val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SavePermissionResult -> onPermissionResult(isGranted = event.isGranted, permission = event.permission)
        }
    }

    /**
     * Dismiss a the dialog. It pops the first entry of the queue of permissions
     */
    fun dismissDialog(){
        visiblePermissionDialogQueue.removeFirst()
    }

    /**
     * Function called when we got the permission result
     * @param permission The string name representing the permission
     * @param isGranted A boolean value used to evaluate if the permission was granted or not
     */
    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ){
        if(!isGranted) visiblePermissionDialogQueue.add(permission)
    }
}
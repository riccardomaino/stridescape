package it.unito.progmob.home.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import it.unito.progmob.home.presentation.HomeEvent

class HomeViewModel: ViewModel() {
    // Queue used to show some potential different permission dialog
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SavePermissionResult -> onPermissionResult(isGranted = event.isGranted, permission = event.permission)
            is HomeEvent.DismissPermissionDialog -> dismissDialog()
        }
    }

    /**
     * Dismiss a the dialog. It pops the first entry of the queue of permissions
     */
    private fun dismissDialog(){
        val removed = visiblePermissionDialogQueue.removeFirst()
        Log.d("HomeViewModel", "dismissDialog: $removed")
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
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) visiblePermissionDialogQueue.add(permission)
    }

}
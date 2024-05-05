package it.unito.progmob.home.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import android.Manifest
import androidx.lifecycle.viewModelScope
import it.unito.progmob.home.presentation.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    // Queue used to show some potential different permission dialog
    var visiblePermissionDialogQueue = mutableStateListOf<String>()
        private set

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CheckPermissionResult -> onPermissionResult(isGranted = event.isGranted, permission = event.permission)
            is HomeEvent.DismissPermissionDialog -> dismissDialog()
        }
    }

    /**
     * Dismiss a the dialog. It pops the first entry of the queue of permissions
     */
    private fun dismissDialog(){
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
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

}
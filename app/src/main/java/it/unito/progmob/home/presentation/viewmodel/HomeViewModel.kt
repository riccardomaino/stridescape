package it.unito.progmob.home.presentation.viewmodel

import android.Manifest
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.presentation.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    // Queue used to contain a list of permission to request again if the the user has refused them
    var visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
        private set

    // Array of permissions to request computed based on the SDK version
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    /**
     * Handles Home events emitted from the UI.
     * @param event The HomeEvent to be processed.
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.PermissionResult -> permissionResult(
                isGranted = event.isGranted,
                permission = event.permission
            )

            is HomeEvent.DismissPermissionDialog -> dismissPermissionDialog()
            is HomeEvent.StartRunningService -> startRunningService()
            is HomeEvent.StopRunningService -> stopRunningService()
        }
    }

    private fun stopRunningService() {
        TODO("Not yet implemented")
    }

    private fun startRunningService() {
        TODO("Not yet implemented")
    }

    /**
     * Calls the use case to dismiss the permission dialog. It pops the first entry of the queue of permissions.
     * This method launch a coroutine, using Dispatchers.Default, to perform the use case
     */
    private fun dismissPermissionDialog() {
        viewModelScope.launch(Dispatchers.Default) {
            val dismissDialogQueueResult =
                homeUseCases.dismissPermissionDialogUseCase(visiblePermissionDialogQueue.value.toMutableList())

            visiblePermissionDialogQueue.update {
                dismissDialogQueueResult
            }
        }
    }


    /**
     * Calls the use case to handle the result of the permission rationale. It adds the permission
     * to the queue of permissions if it is not granted.
     * This method launch a coroutine, using Dispatchers.Default, to perform the use case
     * @param permission The string name representing the permission
     * @param isGranted A boolean value used to evaluate if the permission was granted or not
     */
    private fun permissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val permissionDialogQueueResult = homeUseCases.permissionResultUseCase(
                visiblePermissionDialogQueue.value.toMutableList(),
                permission,
                isGranted
            )

            visiblePermissionDialogQueue.update {
                permissionDialogQueueResult
            }
        }
    }
}
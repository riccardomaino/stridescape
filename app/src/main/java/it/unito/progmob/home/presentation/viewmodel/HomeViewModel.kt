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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    // MutableStateFlow of List<String> managed like a queue used to contain a list of permission to
    // request again if the the user has refused them
    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    lateinit var currentDay: MutableStateFlow<Int>

    val visiblePermissionDialogQueue = _visiblePermissionDialogQueue.asStateFlow()

    var stepsCount = MutableStateFlow(0)

    // Array of permissions to request computed based on the SDK version
    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.POST_NOTIFICATIONS
            add(Manifest.permission.ACTIVITY_RECOGNITION)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }.toTypedArray()


    init {
        val calendarInfo = Calendar.getInstance()
        calendarInfo.time = Date() // your date is an object of type Date
        currentDay = MutableStateFlow(calendarInfo[Calendar.DAY_OF_WEEK]-2)
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
        }
    }

    /**
     * Calls the use case to dismiss the permission dialog. It pops the first entry of the queue of permissions.
     * This method launch a coroutine, using Dispatchers.Default, to perform the use case
     */
    private fun dismissPermissionDialog() {
        viewModelScope.launch(Dispatchers.Default) {
            val dismissDialogQueueResult = homeUseCases.dismissPermissionDialogUseCase(
                    _visiblePermissionDialogQueue.value.toMutableList()
                )

            _visiblePermissionDialogQueue.update {
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
                _visiblePermissionDialogQueue.value.toMutableList(),
                permission,
                isGranted
            )

            _visiblePermissionDialogQueue.update {
                permissionDialogQueueResult
            }
        }
    }
}
package it.unito.progmob.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.presentation.HomeEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    // MutableStateFlow of List<String> managed like a queue used to contain a list of permission to
    // request again if the the user has refused them
    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue = _visiblePermissionDialogQueue.asStateFlow()

    private val _currentDayOfWeek = MutableStateFlow(DateUtils.getCurrentDayOfWeek())
    val currentDayOfWeek = _currentDayOfWeek.asStateFlow()

    private val currentDate = DateUtils.getCurrentDate(pattern = "yyyy/MM/dd")

    val stepsCurrentDay = homeUseCases.getDayStepsUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val caloriesCurrentDay = homeUseCases.getDayCaloriesUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val distanceCurrentDay = homeUseCases.getDayDistanceUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val timeCurrentDay = homeUseCases.getDayTimeUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0L)

    val stepsTargetCurrentDay = homeUseCases.getDateTargetUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    val weeklySteps = homeUseCases.getWeeklyStepsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), intArrayOf())

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
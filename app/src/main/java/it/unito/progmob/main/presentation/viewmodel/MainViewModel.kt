package it.unito.progmob.main.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.main.domain.usecase.MainUseCases
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.main.presentation.MainEvent
import it.unito.progmob.core.presentation.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases,
) : ViewModel() {

    // MutableStateFlow of List<String> managed like a queue used to contain a list of permission to
    // request again if the the user has refused them
    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue = _visiblePermissionDialogQueue.asStateFlow()

    // MutableStateFlow of String used to store the start destination of the app
    var startDestination by mutableStateOf(Route.OnBoardingNavigationRoute.route)
        private set

    // MutableStateFlow of Boolean used for the splash screen to know when the app is ready
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        mainUseCases.readOnboardingEntryUseCase().onEach { shouldStartFromHomeScreen ->
            startDestination = if(shouldStartFromHomeScreen) {
                Route.MainNavigationRoute.route
            } else {
                Route.OnBoardingNavigationRoute.route
            }
            delay(600)
            _isReady.update { true }
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            mainUseCases.checkTargetExistUseCase(DateUtils.getCurrentDateFormatted())
        }
    }

    /**
     * Handles Main events emitted from the UI.
     * @param event The MainEvent to be processed.
     */
    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.PermissionResult -> permissionResult(
                isGranted = event.isGranted,
                permission = event.permission
            )
            is MainEvent.DismissPermissionDialog -> dismissPermissionDialog()
        }
    }

    /**
     * It pops the first entry of the queue of permissions. This method launch a coroutine, using
     * Dispatchers.Default, to perform the operation
     */
    private fun dismissPermissionDialog() {
        viewModelScope.launch(Dispatchers.Default) {
            _visiblePermissionDialogQueue.update {
                it.toMutableList().apply {
                    removeFirst()
                }
            }
        }
    }

    /**
     * It adds the permission to the queue of permissions if it is not granted and it is not already
     * present in the queue. This method launch a coroutine, using Dispatchers.Default, to perform
     * the operation
     *
     * @param permission The string name representing the permission
     * @param isGranted A boolean value used to evaluate if the permission was granted or not
     */
    private fun permissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        viewModelScope.launch {
            if(!isGranted && !_visiblePermissionDialogQueue.value.contains(permission)) {
                _visiblePermissionDialogQueue.update {
                    it.toMutableList().apply {
                        add(permission)
                    }
                }
            }
        }
    }
}
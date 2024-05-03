package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the OnBoarding screen. Handles user interactions and interacts with OnBoardingUseCases
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases
) : ViewModel() {
    // Queue used to show some potential different permission dialog
    private val visiblePermissionDialogQueue = mutableStateListOf<String>()

    /**
     * Handles OnBoarding events emitted from the UI.
     * @param event The OnBoardingEvent to be processed.
     */
    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.SaveOnBoardingEntry -> saveOnboardingEntry()
            is OnBoardingEvent.ReadOnBoardingEntry -> readOnboardingEntry()
            is OnBoardingEvent.SavePermissionResult -> onPermissionResult(isGranted = event.isGranted, permission = event.permission)
        }
    }

    /**
     * Calls the use case to save the onboarding entry value to the user preferences DataStore.
     * This method launch a coroutine, using Dispatchers.IO, to perform the use case
     */
    private fun saveOnboardingEntry() {
        viewModelScope.launch(Dispatchers.IO) {
            onBoardingUseCases.saveOnboardingEntryUseCase()
        }
    }

    /**
     * Calls the use case to read the onboarding entry value from the user preferences DataStore.
     * This method launch a coroutine, using Dispatchers.IO, to perform the use case
     */
    private fun readOnboardingEntry() {
        viewModelScope.launch(Dispatchers.IO) {
            onBoardingUseCases.readOnboardingEntryUseCase()
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
        visiblePermissionDialogQueue.add(0, permission)
    }

}
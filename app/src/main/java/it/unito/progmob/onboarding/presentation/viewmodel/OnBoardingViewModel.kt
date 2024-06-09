package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class that handles the onboarding feature.
 *
 * @param onBoardingUseCases The use cases related to the OnBoarding feature.
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases,
) : ViewModel() {

    // MutableState of the user name, weight and height used to store the user preferences entries values
    val userName: MutableState<String> = mutableStateOf("")
    val userWeight: MutableState<String> = mutableStateOf("")
    val userHeight: MutableState<String> = mutableStateOf("")

    /**
     * Handles OnBoarding events emitted from the UI.
     * @param event The OnBoardingEvent to be processed.
     */
    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.SaveEntries -> saveEntries()
        }
    }

    /**
     * Calls the use cases to save all the user preferences entries values to the user preferences DataStore.
     * This method launch a coroutine, using Dispatchers.IO, which launch all the others coroutines to perform the work
     */
    private fun saveEntries() {
        viewModelScope.launch(Dispatchers.IO) {
            launch { onBoardingUseCases.saveUsernameEntryUseCase(userName.value) }
            launch { onBoardingUseCases.saveUserHeightEntryUseCase(userHeight.value) }
            launch { onBoardingUseCases.saveUserWeightEntryUseCase(userWeight.value) }
            launch { onBoardingUseCases.saveOnboardingEntryUseCase() }
        }
    }
}
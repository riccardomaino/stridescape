package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases
) : ViewModel() {
    /**
     * Handles OnBoarding events emitted from the UI.
     * @param event The OnBoardingEvent to be processed.
     */
    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.SaveOnBoardingEntry -> saveOnboardingEntry()
            is OnBoardingEvent.ReadOnBoardingEntry -> readOnboardingEntry()
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

}
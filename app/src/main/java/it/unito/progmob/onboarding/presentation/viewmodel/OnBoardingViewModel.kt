package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
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
    private val onBoardingUseCases: OnBoardingUseCases,
) : ViewModel() {

    val weight = mutableStateOf("")
    val height = mutableStateOf("")
    val name = mutableStateOf("")

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
            launch { onBoardingUseCases.saveUserNameUseCase(name.value) }
            launch { onBoardingUseCases.saveUserHeightUseCase(height.value) }
            launch { onBoardingUseCases.saveUserHeightUseCase(weight.value) }
            launch { onBoardingUseCases.saveOnboardingEntryUseCase() }
        }
    }
}
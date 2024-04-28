package it.unito.progmob.onboarding.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.onboarding.domain.usecases.ReadOnboardingEntry
import it.unito.progmob.onboarding.domain.usecases.SaveOnboardingEntry
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val saveOnboardingEntry: SaveOnboardingEntry,
    private val readOnboardingEntry: ReadOnboardingEntry
): ViewModel() {

    fun onEvent(event: OnBoardingEvent) {
        when(event){
            is OnBoardingEvent.SaveOnboardingEntry -> saveOnboardingCompleted()
            is OnBoardingEvent.ReadOnboardingEntry -> readOnboardingCompleted()
        }
    }

    private fun saveOnboardingCompleted() {
        viewModelScope.launch {
            saveOnboardingEntry()
        }
    }

    private fun readOnboardingCompleted() {
        viewModelScope.launch {
            readOnboardingEntry()
        }
    }

}
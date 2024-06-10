package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import it.unito.progmob.onboarding.presentation.state.UiOnBoardingState
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

    var onBoardingState = mutableStateOf(UiOnBoardingState())
        private set

    /**
     * Handles Profile events emitted from the UI.
     * @param event The ProfileEvent to be processed.
     */
    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.HeightChanged -> validateHeight(event.height)
            is OnBoardingEvent.TargetChanged -> validateTarget(event.target)
            is OnBoardingEvent.UsernameChanged -> validateUsername(event.username)
            is OnBoardingEvent.WeightChanged -> validateWeight(event.weight)
            is OnBoardingEvent.SaveProfile -> saveProfile()
        }
    }

    private fun saveProfile() {
        viewModelScope.launch(Dispatchers.Default) {
            if (onBoardingState.value.usernameError == null &&
                onBoardingState.value.heightError == null &&
                onBoardingState.value.weightError == null &&
                onBoardingState.value.targetError == null
            ) {
                launch { onBoardingUseCases.saveUsernameEntryUseCase(onBoardingState.value.username) }
                launch { onBoardingUseCases.saveUserHeightEntryUseCase(onBoardingState.value.height) }
                launch { onBoardingUseCases.saveUserWeightEntryUseCase(onBoardingState.value.weight) }
                launch { onBoardingUseCases.updateTargetUseCase(onBoardingState.value.target) }
                launch { onBoardingUseCases.saveOnboardingEntryUseCase()}
            }
        }
    }

    private fun validateWeight(weight: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateWeightUseCase(weight)
            onBoardingState.value = onBoardingState.value.copy(
                weight = weight,
                weightError = validationResult.message
            )
        }
    }

    private fun validateUsername(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateUsernameUseCase(username)
            onBoardingState.value = onBoardingState.value.copy(
                username = username,
                usernameError = validationResult.message
            )
        }
    }

    private fun validateTarget(target: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateTargetUseCase(target)
            onBoardingState.value = onBoardingState.value.copy(
                target = target,
                targetError = validationResult.message
            )
        }
    }

    private fun validateHeight(height: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateHeightUseCase(height)
            onBoardingState.value = onBoardingState.value.copy(
                height = height,
                heightError = validationResult.message
            )
        }
    }
}
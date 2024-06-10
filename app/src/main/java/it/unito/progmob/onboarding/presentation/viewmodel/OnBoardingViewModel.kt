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
 * ViewModel for the OnBoarding screen.
 *
 * This ViewModel handles events emitted from the UI and updates the [onBoardingState] accordingly.
 * It also validates user input and saves the user's profile information.
 *
 * @param onBoardingUseCases The use cases for the OnBoarding screen.
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases,
) : ViewModel() {

    /**
     * The current state of the OnBoarding screen.
     */
    var onBoardingState = mutableStateOf(UiOnBoardingState())
        private set

    /**
     * Handles Profile events emitted from the UI.
     *
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

    /**
     * Saves the user's profile information.
     */
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

    /**
     * Validates the user's weight input.
     *
     * @param weight The user's weight input.
     */
    private fun validateWeight(weight: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateWeightUseCase(weight)
            onBoardingState.value = onBoardingState.value.copy(
                weight = weight,
                weightError = validationResult.message
            )
        }
    }

    /**
     * Validates the user's username input.
     *
     * @param username The user's username input.
     */
    private fun validateUsername(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateUsernameUseCase(username)
            onBoardingState.value = onBoardingState.value.copy(
                username = username,
                usernameError = validationResult.message
            )
        }
    }

    /**
     * Validates the user's target input.
     *
     * @param target The user's target input.
     */
    private fun validateTarget(target: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = onBoardingUseCases.validateTargetUseCase(target)
            onBoardingState.value = onBoardingState.value.copy(
                target = target,
                targetError = validationResult.message
            )
        }
    }

    /**
     * Validates the user's height input.
     *
     * @param height The user's height input.
     */
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
package it.unito.progmob.onboarding.presentation

/**
 * Sealed class that represents the possible UI events that can be triggered in the onboarding
 * feature.
 */
sealed class OnBoardingEvent {
    data object SaveProfile: OnBoardingEvent()
    data class HeightChanged(val height: String) : OnBoardingEvent()
    data class WeightChanged(val weight: String) : OnBoardingEvent()
    data class TargetChanged(val target: String) : OnBoardingEvent()
    data class UsernameChanged(val username: String) : OnBoardingEvent()
}
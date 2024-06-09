package it.unito.progmob.onboarding.presentation

/**
 * Sealed class that represents the possible UI events that can be triggered in the onboarding
 * feature.
 */
sealed class OnBoardingEvent {
    data object SaveEntries: OnBoardingEvent()
}
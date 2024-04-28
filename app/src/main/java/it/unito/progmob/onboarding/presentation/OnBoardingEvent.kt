package it.unito.progmob.onboarding.presentation

sealed class OnBoardingEvent {
    data object SaveOnboardingEntry : OnBoardingEvent()
    data object ReadOnboardingEntry : OnBoardingEvent()
}
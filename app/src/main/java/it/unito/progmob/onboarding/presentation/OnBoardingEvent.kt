package it.unito.progmob.onboarding.presentation

sealed class OnBoardingEvent {
    data object SaveEntries: OnBoardingEvent()
}
package it.unito.progmob.onboarding.presentation

sealed class OnBoardingEvent {
    data object SaveOnBoardingEntry : OnBoardingEvent()
    data object ReadOnBoardingEntry : OnBoardingEvent()
}
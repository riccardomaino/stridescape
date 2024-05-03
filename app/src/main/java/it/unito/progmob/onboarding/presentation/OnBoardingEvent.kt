package it.unito.progmob.onboarding.presentation

sealed class OnBoardingEvent {
    data object SaveOnBoardingEntry : OnBoardingEvent()
    data object ReadOnBoardingEntry : OnBoardingEvent()
    data class SavePermissionResult(val permission : String, val isGranted : Boolean) : OnBoardingEvent()
}
package it.unito.progmob.profile.presentation

sealed class ProfileEvent {
    data class UsernameChanged(val username: String): ProfileEvent()
    data class HeightChanged(val height: String): ProfileEvent()
    data class WeightChanged(val weight: String): ProfileEvent()
    data class TargetChanged(val target: String): ProfileEvent()
    data object SaveProfile: ProfileEvent()

}
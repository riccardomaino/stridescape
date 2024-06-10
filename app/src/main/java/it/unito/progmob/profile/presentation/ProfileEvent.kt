package it.unito.progmob.profile.presentation

/**
 * Sealed class representing events that can occur on the profile screen.
 */
sealed class ProfileEvent {
    /**
     * Event triggered when the username is changed.
     *
     * @param username The new username.
     */
    data class UsernameChanged(val username: String): ProfileEvent()
    /**
     * Event triggered when the height is changed.
     *
     * @param height The new height.
     */
    data class HeightChanged(val height: String): ProfileEvent()
    /**
     * Event triggered when the weight is changed.
     *
     * @param weight The new weight.
     */
    data class WeightChanged(val weight: String): ProfileEvent()
    /**
     * Event triggered when the target is changed.
     *
     * @param target The new target.
     */
    data class TargetChanged(val target: String): ProfileEvent()
    /**
     * Event triggered when the save button is clicked.
     */
    data object SaveProfile: ProfileEvent()
}
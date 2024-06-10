package it.unito.progmob.onboarding.presentation

/**
 * Sealed class that represents the possible UI events that can be triggered in the onboarding
 * feature.
 */
sealed class OnBoardingEvent {
    /**
     * Event triggered when the user saves their profile.
     */
    data object SaveProfile: OnBoardingEvent()
    /**
     * Event triggered when the user changes their height.
     *
     * @property height The new height value.
     */
    data class HeightChanged(val height: String) : OnBoardingEvent()
    /**
     * Event triggered when the user changes their weight.
     *
     * @property weight The new weight value.
     */
    data class WeightChanged(val weight: String) : OnBoardingEvent()
    /**
     * Event triggered when the user changes their target.
     *
     * @property target The new target value.
     */
    data class TargetChanged(val target: String) : OnBoardingEvent()
    /**
     * Event triggered when the user changes their username.
     *
     * @property username The new username value.
     */
    data class UsernameChanged(val username: String) : OnBoardingEvent()
}
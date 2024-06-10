package it.unito.progmob.onboarding.presentation.state

import it.unito.progmob.core.presentation.util.UiText

/**
 * Data class representing the UI state of the OnBoarding screen.
 *
 * @param username The user's username.
 * @param usernameError The error message for the username input, if any.
 * @param height The user's height.
 * @param heightError The error message for the height input, if any.
 * @param weight The user's weight.
 * @param weightError The error message for theweight input, if any.
 * @param target The user's target.
 * @param targetError The error message for the target input, if any.
 */
data class UiOnBoardingState(
    var username: String = "",
    val usernameError: UiText? = null,
    var height: String = "",
    val heightError: UiText? = null,
    var weight: String = "",
    val weightError: UiText? = null,
    var target: String = "",
    val targetError: UiText? = null,
)
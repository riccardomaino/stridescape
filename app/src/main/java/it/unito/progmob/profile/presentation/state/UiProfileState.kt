package it.unito.progmob.profile.presentation.state

import it.unito.progmob.core.presentation.util.UiText

/**
 * Data class representing the UI state for the profile screen.
 *
 * @param username The current username.
 * @param usernameError The error message for the username, if any.
 * @param height The current height.
 * @param heightError The error message for the height, if any.
 * @param weight The current weight.
 * @param weightError The error message for the weight, if any.
 * @param target The current target.
 * @param targetError The error message for the target, if any.
 */
data class UiProfileState(
    var username: String = "",
    val usernameError: UiText? = null,
    var height: String = "",
    val heightError: UiText? = null,
    var weight: String = "",
    val weightError: UiText? = null,
    var target: String = "",
    val targetError: UiText? = null,
)
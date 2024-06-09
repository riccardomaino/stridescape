package it.unito.progmob.profile.presentation.state

import it.unito.progmob.core.presentation.util.UiText

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
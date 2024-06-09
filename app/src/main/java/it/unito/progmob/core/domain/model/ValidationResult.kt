package it.unito.progmob.core.domain.model

import it.unito.progmob.core.presentation.util.UiText

data class ValidationResult (
    val successful: Boolean,
    val message: UiText? = null
)
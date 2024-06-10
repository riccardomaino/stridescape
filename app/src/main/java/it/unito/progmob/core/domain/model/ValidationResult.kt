package it.unito.progmob.core.domain.model

import it.unito.progmob.core.presentation.util.UiText

/**
 * Data class representing the result of a validation operation.
 *
 * @property successful Indicates whether the validation was successful.
 * @property message An optional message providing more context about the validation result,
 *                   typically displayed to the user.
 */
data class ValidationResult (
    val successful: Boolean,
    val message: UiText? = null
)
package it.unito.progmob.core.domain.usecase

import it.unito.progmob.R
import it.unito.progmob.core.domain.model.ValidationResult
import it.unito.progmob.core.presentation.util.UiText

class ValidateUsernameUseCase {
    /**
     * Validates the username input.
     *
     * @param username The username string to validate.
     * @return [ValidationResult] indicating the validation status and message.
     */
    operator fun invoke(username: String): ValidationResult {
        // Check if the username is blank.
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_blank_username)
            )
        }

        if (username.length > 15) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_long_username)
            )
        }
        // If all checks pass, return a successful validation result.
        return ValidationResult(
            successful = true,
            message = null
        )
    }
}
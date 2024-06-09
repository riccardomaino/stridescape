package it.unito.progmob.core.domain.usecase

import it.unito.progmob.R
import it.unito.progmob.core.domain.model.ValidationResult
import it.unito.progmob.core.domain.util.ValidatorUtils.isInteger
import it.unito.progmob.core.presentation.util.UiText

/**
 * Use case to validate the height input.
 */
class ValidateHeightUseCase {
    /**
     * Validates the height input.
     *
     * @param height The height string to validate.
     * @return [ValidationResult] indicating the validation status and message.
     */
    operator fun invoke(height: String): ValidationResult {
        // Check if the height is blank.
        if (height.isBlank()) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_blank_height)
            )
        }
        // Check if the height is a valid integer.
        if (!isInteger(height)) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_invalid_number_height)
            )
        }
        // Check if the height is within the valid range (25-250).
        if(height.toInt() < 25 || height.toInt() > 250){
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_invalid_range_height)
            )
        }

        // If all checks pass, return a successful validation result.
        return ValidationResult(
            successful = true,
            message = null
        )
    }
}
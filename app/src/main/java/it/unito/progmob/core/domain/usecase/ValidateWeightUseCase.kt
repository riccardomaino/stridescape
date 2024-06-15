package it.unito.progmob.core.domain.usecase

import it.unito.progmob.R
import it.unito.progmob.core.domain.model.ValidationResult
import it.unito.progmob.core.domain.util.ValidatorUtils.isInteger
import it.unito.progmob.core.presentation.util.UiText

class ValidateWeightUseCase {
    /**
     * Validates the weight input.
     *
     * @param weight The weight string to validate.
     * @return [ValidationResult] indicating the validation status and message.
     */
    operator fun invoke(weight: String): ValidationResult {
        // Check if the weight is blank.
        if (weight.isBlank()) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_blank_weight)
            )
        }
        // Check if the weight is a valid integer.
        if (!isInteger(weight)) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_invalid_number_weight)
            )
        }
        // Check if the weight is within the valid range (15-300).
        val weightConverted = weight.toIntOrNull()
        weightConverted?.let {
            if(it < 15 || it > 300){
                return ValidationResult(
                    successful = false,
                    message = UiText.StringResource(resId = R.string.validation_error_invalid_range_weight)
                )
            }
        } ?: return ValidationResult(
            successful = false,
            message = UiText.StringResource(resId = R.string.validation_error_invalid_number_weight)
        )

        // If all checks pass, return a successful validation result.
        return ValidationResult(
            successful = true,
            message = null
        )
    }
}
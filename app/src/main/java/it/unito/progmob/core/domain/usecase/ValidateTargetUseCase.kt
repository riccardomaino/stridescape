package it.unito.progmob.core.domain.usecase

import it.unito.progmob.R
import it.unito.progmob.core.domain.model.ValidationResult
import it.unito.progmob.core.domain.util.ValidatorUtils.isInteger
import it.unito.progmob.core.presentation.util.UiText

class ValidateTargetUseCase {
    /**
     * Validates the target input.
     *
     * @param target The target string to validate.
     * @return [ValidationResult] indicating the validation status and message.
     */
    operator fun invoke(target: String): ValidationResult {
        // Check if the target is blank.
        if (target.isBlank()) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_blank_target)
            )
        }
        // Check if the target is a valid integer.
        if (!isInteger(target)) {
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_invalid_number_target)
            )
        }
        // Check if the target is within the valid range (>0).
        if(target.toInt() < 1){
            return ValidationResult(
                successful = false,
                message = UiText.StringResource(resId = R.string.validation_error_invalid_range_target)
            )
        }

        // If all checks pass, return a successful validation result.
        return ValidationResult(
            successful = true,
            message = null
        )
    }
}
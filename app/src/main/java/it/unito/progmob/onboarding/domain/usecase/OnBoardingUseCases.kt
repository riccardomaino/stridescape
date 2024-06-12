package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ValidateHeightUseCase
import it.unito.progmob.core.domain.usecase.ValidateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateUsernameUseCase
import it.unito.progmob.core.domain.usecase.ValidateWeightUseCase
import it.unito.progmob.core.domain.usecase.UpdateTargetUseCase

/**
 * Data class that holds all use cases related to the onboarding feature.
 */
data class OnBoardingUseCases(
    val saveOnboardingEntryUseCase: SaveOnboardingEntryUseCase,
    val saveUsernameEntryUseCase: SaveUsernameEntryUseCase,
    val saveUserWeightEntryUseCase: SaveUserWeightEntryUseCase,
    val saveUserHeightEntryUseCase: SaveUserHeightEntryUseCase,
    val updateTargetUseCase: UpdateTargetUseCase,
    val validateHeightUseCase: ValidateHeightUseCase,
    val validateTargetUseCase: ValidateTargetUseCase,
    val validateUsernameUseCase: ValidateUsernameUseCase,
    val validateWeightUseCase: ValidateWeightUseCase
)
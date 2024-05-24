package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveOnboardingEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserNameEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase

/**
 * Data class that holds all use cases related to the onboarding feature.
 */
data class OnBoardingUseCases(
    val readOnboardingEntryUseCase: ReadOnboardingEntryUseCase,
    val saveOnboardingEntryUseCase: SaveOnboardingEntryUseCase,
    val saveUserNameEntryUseCase: SaveUserNameEntryUseCase,
    val saveUserWeightEntryUseCase: SaveUserWeightEntryUseCase,
    val saveUserHeightEntryUseCase: SaveUserHeightEntryUseCase,
)

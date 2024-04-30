package it.unito.progmob.onboarding.domain.usecase

/**
 * Data class that holds all use cases related to the onboarding feature.
 */
data class OnBoardingUseCases(
    val readOnboardingEntryUseCase: ReadOnboardingEntryUseCase,
    val saveOnboardingEntryUseCase: SaveOnboardingEntryUseCase
)

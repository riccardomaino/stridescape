package it.unito.progmob.main.domain.usecase

/**
 * Data class that holds all use cases related to the main feature.
 */
data class MainUseCases(
    val readOnboardingEntryUseCase: ReadOnboardingEntryUseCase,
    val checkTargetExistUseCase: CheckTargetExistUseCase
)

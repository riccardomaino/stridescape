package it.unito.progmob.core.domain.usecase

data class MainUseCases(
    val readOnboardingEntryUseCase: ReadOnboardingEntryUseCase,
    val checkTargetExistUseCase: CheckTargetExistUseCase,
    val dismissPermissionDialogUseCase: DismissPermissionDialogUseCase,
    val permissionResultUseCase: PermissionResultUseCase
)

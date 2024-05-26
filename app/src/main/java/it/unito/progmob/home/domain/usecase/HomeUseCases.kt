package it.unito.progmob.home.domain.usecase

/**
 * Data class that holds all use cases related to the home feature.
 */
data class HomeUseCases(
    val dismissPermissionDialogUseCase: DismissPermissionDialogUseCase,
    val permissionResultUseCase: PermissionResultUseCase,
    val getCurrentDayStepsUseCase: GetCurrentDayStepsUseCase,
    val getCurrentDayCaloriesUseCase: GetCurrentDayCaloriesUseCase,
    val getCurrentDayDistanceUseCase: GetCurrentDayDistanceUseCase,
    val getCurrentDayTimeUseCase: GetCurrentDayTimeUseCase
)

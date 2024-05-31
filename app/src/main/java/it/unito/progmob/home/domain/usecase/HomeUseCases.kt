package it.unito.progmob.home.domain.usecase

/**
 * Data class that holds all use cases related to the home feature.
 */
data class HomeUseCases(
    val dismissPermissionDialogUseCase: DismissPermissionDialogUseCase,
    val permissionResultUseCase: PermissionResultUseCase,
    val getDayStepsUseCase: GetDayStepsUseCase,
    val getDayCaloriesUseCase: GetDayCaloriesUseCase,
    val getDayDistanceUseCase: GetDayDistanceUseCase,
    val getDayTimeUseCase: GetDayTimeUseCase,
    val addTargetUseCase: AddTargetUseCase,
    val getDateTargetUseCase: GetDateTargetUseCase,
    val getWeeklyStepsUseCase: GetWeeklyStepsUseCase,
)

package it.unito.progmob.home.domain.usecase

/**
 * Data class that holds all use cases related to the home feature.
 */
data class HomeUseCases(
    val getDayStepsUseCase: GetDayStepsUseCase,
    val getDayCaloriesUseCase: GetDayCaloriesUseCase,
    val getDayDistanceUseCase: GetDayDistanceUseCase,
    val getDayTimeUseCase: GetDayTimeUseCase,
    val getDateTargetUseCase: GetDateTargetUseCase,
    val getWeeklyStepsUseCase: GetWeeklyStepsUseCase,
    val getWeeklyTargetUseCase: GetWeeklyTargetUseCase
)

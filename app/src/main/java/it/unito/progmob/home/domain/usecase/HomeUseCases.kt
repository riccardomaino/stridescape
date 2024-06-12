package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.usecase.GetTargetUseCase

/**
 * Data class that holds all use cases related to the home feature.
 */
data class HomeUseCases(
    val getDayStepsUseCase: GetDayStepsUseCase,
    val getDayCaloriesUseCase: GetDayCaloriesUseCase,
    val getDayDistanceUseCase: GetDayDistanceUseCase,
    val getDayTimeUseCase: GetDayTimeUseCase,
    val getTargetUseCase: GetTargetUseCase,
    val getWeeklyStepsUseCase: GetWeeklyStepsUseCase,
    val getWeeklyTargetUseCase: GetWeeklyTargetUseCase
)

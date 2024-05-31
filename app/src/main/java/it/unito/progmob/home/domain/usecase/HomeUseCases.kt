package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.usecase.DismissPermissionDialogUseCase
import it.unito.progmob.core.domain.usecase.PermissionResultUseCase

/**
 * Data class that holds all use cases related to the home feature.
 */
data class HomeUseCases(
    val getDayStepsUseCase: GetDayStepsUseCase,
    val getDayCaloriesUseCase: GetDayCaloriesUseCase,
    val getDayDistanceUseCase: GetDayDistanceUseCase,
    val getDayTimeUseCase: GetDayTimeUseCase,
    val addTargetUseCase: AddTargetUseCase,
    val getDateTargetUseCase: GetDateTargetUseCase
)

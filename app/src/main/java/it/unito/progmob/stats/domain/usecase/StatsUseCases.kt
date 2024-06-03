package it.unito.progmob.stats.domain.usecase

data class StatsUseCases(
    val getDistanceDataUseCase: GetDistanceDataUseCase,
    val getTimeDataUseCase: GetTimeDataUseCase,
    val getCaloriesDataUseCase: GetCaloriesDataUseCase,
    val getStepsDataUseCase: GetStepsDataUseCase,
    val getSpeedDataUseCase: GetSpeedDataUseCase
)
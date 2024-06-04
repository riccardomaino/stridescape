package it.unito.progmob.stats.domain.usecase

data class StatsUseCases(
    val getWeekOrMonthDistanceStatUseCase: GetWeekOrMonthDistanceStatUseCase,
    val getWeekOrMonthTimeStatUseCase: GetWeekOrMonthTimeStatUseCase,
    val getWeekOrMonthCaloriesStatUseCase: GetWeekOrMonthCaloriesStatUseCase,
    val getWeekOrMonthStepsStatUseCase: GetWeekOrMonthStepsStatUseCase,
    val getWeekOrMonthSpeedStatUseCase: GetWeekOrMonthSpeedStatUseCase,
    val getYearDistanceStatUseCase: GetYearDistanceStatUseCase,
    val getYearTimeStatUseCase: GetYearTimeStatUseCase,
    val getYearCaloriesStatUseCase: GetYearCaloriesStatUseCase,
    val getYearStepsStatUseCase: GetYearStepsStatUseCase,
    val getYearSpeedStatUseCase: GetYearSpeedStatUseCase
)
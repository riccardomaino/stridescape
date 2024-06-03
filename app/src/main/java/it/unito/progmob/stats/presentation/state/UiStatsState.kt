package it.unito.progmob.stats.presentation.state

import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.StatsType
import kotlinx.datetime.LocalDate

data class UiStatsState(
    val startDate: Long = DateUtils.getInstantOfDateFromNow(6, DateUtils.DateOperation.MINUS).toEpochMilliseconds(),
    val endDate: Long = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds(),
    val statsSelected: StatsType = StatsType.DISTANCE,
    val distanceChartValues: List<Pair<LocalDate, Float>> = emptyList(),
    val timeChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val caloriesChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val stepsChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val speedChartValues: List<Pair<LocalDate, Float>> = emptyList()
)
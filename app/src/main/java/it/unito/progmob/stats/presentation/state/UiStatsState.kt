package it.unito.progmob.stats.presentation.state

import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType
import kotlinx.datetime.LocalDate

data class UiStatsState(
    val rangeSelected: RangeType = RangeType.WEEK,
    val statsSelected: StatsType = StatsType.STEPS,
    val distanceChartValues: List<Pair<LocalDate, Float>> = emptyList(),
    val timeChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val caloriesChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val stepsChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val speedChartValues: List<Pair<LocalDate, Float>> = emptyList()
)
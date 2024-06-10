package it.unito.progmob.stats.presentation.state

import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType
import kotlinx.datetime.LocalDate

/**
 * Data class representing the UI state for the statistics screen.
 *
 * @param rangeSelected The currently selected range.
 * @param statsSelected The currently selected statistic.
 * @param distanceChartValues The values for the distance chart.
 * @param timeChartValues The values for the time chart.
 * @param caloriesChartValues The values for the calories chart.
 * @param stepsChartValues The values for the steps chart.
 * @param speedChartValues The values for the speed chart.
 */
data class UiStatsState(
    val rangeSelected: RangeType = RangeType.WEEK,
    val statsSelected: StatsType = StatsType.STEPS,
    val distanceChartValues: List<Pair<LocalDate, Float>> = emptyList(),
    val timeChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val caloriesChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val stepsChartValues: List<Pair<LocalDate, Int>> = emptyList(),
    val speedChartValues: List<Pair<LocalDate, Float>> = emptyList()
)
package it.unito.progmob.stats.presentation

import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType

/**
 * Sealed class that represents the possible UI events that can be triggered in the stats
 * feature.
 */
sealed class StatsEvent {
    /**
     * EventTriggered when a new statistic type is selected.
     *
     * @param statsSelected The selected statistic type.
     */
    data class StatsTypeSelected(val statsSelected: StatsType) : StatsEvent()
    /**
     * Event triggered when a new range type is selected.
     *
     ** @param rangeSelected The selected range type.
     */
    data class RangeTypeSelected(val rangeSelected: RangeType) : StatsEvent()
}

package it.unito.progmob.stats.presentation

import it.unito.progmob.stats.domain.model.StatsType

sealed class StatsEvent {
    data class StatsTypeSelected(val statsSelected: StatsType) : StatsEvent()
    data class DateRangeSelected(val startDate: Long, val endDate: Long) : StatsEvent()
}

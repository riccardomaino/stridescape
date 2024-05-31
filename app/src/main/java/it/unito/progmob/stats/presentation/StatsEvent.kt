package it.unito.progmob.stats.presentation

import it.unito.progmob.stats.domain.model.StatsType

sealed class StatsEvent {
    data class FilterSelected(val statsSelected: StatsType) : StatsEvent()
}

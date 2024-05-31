package it.unito.progmob.stats.presentation.state

import it.unito.progmob.stats.domain.model.StatsType
import java.util.Date

data class UiStatsState(
    val initialDate: Date,
    val finalDate: Date,
    val statsSelected: StatsType
){
    companion object {
        val DEFAULT
            get() = UiStatsState(
                initialDate = Date(),
                finalDate = Date(),
                statsSelected = StatsType.DISTANCE
            )
    }
}
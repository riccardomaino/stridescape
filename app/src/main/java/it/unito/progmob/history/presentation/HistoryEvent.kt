package it.unito.progmob.history.presentation

sealed class HistoryEvent {
    data class DateRangeSelected(val startDate: Long, val endDate: Long) : HistoryEvent()
}
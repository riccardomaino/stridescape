package it.unito.progmob.history.presentation

/**
 * A sealed class representing events that can occur in the History screen.
 */
sealed class HistoryEvent {
    /*** Event emitted when a date range is selected in the History screen.
     *
     * @param startDate The start date of the selected range, in milliseconds since the epoch.
     * @param endDate The end date of the selected range, in milliseconds since the epoch.
     */
    data class DateRangeSelected(val startDate: Long, val endDate: Long) : HistoryEvent()
}
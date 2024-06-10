package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of weekday and number of steps.
 *
 * @property weekDay The day of the week asan integer (0 for Monday, 1 for Tuesday, etc.).
 * @property steps The number of steps associated with the weekday.
 */
data class WeekDayStepsTuple (
    @ColumnInfo(name = "weekDay") val weekDay: Int,
    @ColumnInfo(name = "steps") val steps: Int
)
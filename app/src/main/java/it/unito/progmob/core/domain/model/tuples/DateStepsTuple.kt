package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and number of steps.
 *
 * @property date The date as a string.
 * @property steps The number of steps associated with the date.
 */
data class DateStepsTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "steps") val steps: Int
)
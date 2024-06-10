package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and daily step target.
 *
 * @property date The date as a string.
 * @property stepsTarget The daily step target associated with the date.
 */
data class DateTargetTuple (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "stepsTarget") val stepsTarget: Int
)
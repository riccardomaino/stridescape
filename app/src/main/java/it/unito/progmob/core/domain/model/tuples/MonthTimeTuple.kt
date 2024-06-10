package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of month and time.
 *
 * @property month The month as an integer (1 for January, 2 for February, etc.).
 * @property time The time in milliseconds associated with the month.
 */
data class MonthTimeTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "time") val time: Long
)

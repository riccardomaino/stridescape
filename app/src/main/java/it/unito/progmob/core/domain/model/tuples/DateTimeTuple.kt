package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and time.
 *
 * @property date The date as a string.
 * @property time The time in milliseconds.
 */
data class DateTimeTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: Long
)
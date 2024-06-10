package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and average speed.
 *
 * @property date The date as a string.
 *@property averageSpeed The average speed associated with the date.
 */
data class DateSpeedTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "averageSpeed") val averageSpeed: Float
)
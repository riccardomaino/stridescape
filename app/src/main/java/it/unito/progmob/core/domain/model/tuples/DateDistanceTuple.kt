package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and distance.
 *
 * @property date The date as a string.
 *@property distance The distance associated with the date.
 */
data class DateDistanceTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "distance") val distance: Int
)

package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of month and distance.
 *
 * @property month The month as an integer (1 for January, 2 for February, etc.).
 * @property distance The distance associated with the month.
 */
data class MonthDistanceTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "distance") val distance: Int
)

package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of month and average speed.
 *
 * @property month The month as an integer (1 forJanuary, 2 for February, etc.).
 * @property averageSpeed The average speed associated with the month.
 */
data class MonthSpeedTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "averageSpeed") val averageSpeed: Float
)

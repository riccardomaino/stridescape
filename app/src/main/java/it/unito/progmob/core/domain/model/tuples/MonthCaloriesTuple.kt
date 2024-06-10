package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of month and calories.
 *
 * @property month The month as an integer (1 for January, 2 for February, etc.).
 * @property calories The number of calories associated with the month.
 */
data class MonthCaloriesTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "calories") val calories: Int
)

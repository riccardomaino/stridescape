package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of month and number of steps.
 *
 * @property month The month as an integer (1for January, 2 for February, etc.).
 * @property steps The number of steps associated with the month.
 */
data class MonthStepsTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "steps") val steps: Int
)

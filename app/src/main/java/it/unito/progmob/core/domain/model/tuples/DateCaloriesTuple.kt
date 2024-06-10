package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

/**
 * Data class representing a tuple of date and calories.
 *
 * @property date The date as a string.
 * @property calories The number of calories associated with the date.
 */
data class DateCaloriesTuple (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "calories") val calories: Int
)
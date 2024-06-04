package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class DateCaloriesTuple (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "calories") val calories: Int
)
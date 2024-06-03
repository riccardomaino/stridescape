package it.unito.progmob.core.domain.model

import androidx.room.ColumnInfo

data class DateCaloriesTuple (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "calories") val calories: Int
)
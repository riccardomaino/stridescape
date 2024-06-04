package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class MonthCaloriesTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "calories") val calories: Int
)

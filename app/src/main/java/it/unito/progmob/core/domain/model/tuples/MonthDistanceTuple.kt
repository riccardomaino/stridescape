package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class MonthDistanceTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "distance") val distance: Int
)

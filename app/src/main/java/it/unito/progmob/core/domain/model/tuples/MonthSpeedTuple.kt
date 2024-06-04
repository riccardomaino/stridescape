package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class MonthSpeedTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "averageSpeed") val averageSpeed: Float
)

package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class DateSpeedTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "averageSpeed") val averageSpeed: Float
)
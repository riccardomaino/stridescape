package it.unito.progmob.core.domain.model

import androidx.room.ColumnInfo

data class DateDistanceTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "distance") val distance: Int
)

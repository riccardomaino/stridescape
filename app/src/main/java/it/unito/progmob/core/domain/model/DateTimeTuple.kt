package it.unito.progmob.core.domain.model

import androidx.room.ColumnInfo

data class DateTimeTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: Long
)
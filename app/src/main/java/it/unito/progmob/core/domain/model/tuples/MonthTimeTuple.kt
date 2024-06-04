package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class MonthTimeTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "time") val time: Long
)

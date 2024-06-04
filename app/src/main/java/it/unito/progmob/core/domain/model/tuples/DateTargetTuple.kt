package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class DateTargetTuple (
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "stepsTarget") val stepsTarget: Int
)
package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class WeekDayStepsTuple (
    @ColumnInfo(name = "weekDay") val weekDay: Int,
    @ColumnInfo(name = "steps") val steps: Int
)
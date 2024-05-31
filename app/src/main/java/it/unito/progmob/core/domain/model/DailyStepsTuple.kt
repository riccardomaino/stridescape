package it.unito.progmob.core.domain.model

import androidx.room.ColumnInfo

data class DailyStepsTuple (
    @ColumnInfo(name = "weekDay") val weekDay: Int,
    @ColumnInfo(name = "steps") val steps: Int
)
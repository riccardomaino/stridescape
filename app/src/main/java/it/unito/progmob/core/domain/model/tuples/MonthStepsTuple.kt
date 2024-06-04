package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class MonthStepsTuple(
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "steps") val steps: Int
)

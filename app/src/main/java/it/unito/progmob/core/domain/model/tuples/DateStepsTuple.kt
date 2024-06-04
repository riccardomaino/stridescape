package it.unito.progmob.core.domain.model.tuples

import androidx.room.ColumnInfo

data class DateStepsTuple(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "steps") val steps: Int
)
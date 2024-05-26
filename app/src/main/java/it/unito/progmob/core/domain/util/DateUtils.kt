package it.unito.progmob.core.domain.util

import java.util.Calendar
import java.util.Date

object DateUtils {
    fun getCurrentDayOfWeek(): Int {
        val calendarInfo = Calendar.getInstance()
        calendarInfo.time = Date() // your date is an object of type Date
        return calendarInfo[Calendar.DAY_OF_WEEK]-2
    }
}
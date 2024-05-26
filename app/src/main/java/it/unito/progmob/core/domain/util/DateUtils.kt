package it.unito.progmob.core.domain.util

import java.time.Instant
import java.util.Calendar
import java.util.Date

object DateUtils {
    fun getCurrentDayOfWeek(): Int {
        val calendarInfo = Calendar.getInstance()
        calendarInfo.time = Date() // Your date is an object of type Date
        val dayOfWeek = calendarInfo[Calendar.DAY_OF_WEEK]
        return (dayOfWeek-2).mod(7)
    }

    fun getCurrentDate(pattern: String): String {
        val currentTimeStamp = Instant.now().epochSecond
        return TimeUtils.formatEpochTime(currentTimeStamp, pattern)
    }
}
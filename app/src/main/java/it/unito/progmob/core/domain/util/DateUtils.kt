package it.unito.progmob.core.domain.util

import java.time.Instant
import java.util.Calendar
import java.util.Date

object DateUtils {
    fun getCurrentDayOfWeek(): Int {
        val calendarInfo = Date().toCalendar()
        val dayOfWeek = calendarInfo[Calendar.DAY_OF_WEEK]
        return (dayOfWeek-2).mod(7)
    }

    fun getCurrentDate(pattern: String): String {
        val currentTimeStamp = Instant.now().epochSecond
        return TimeUtils.formatEpochTime(currentTimeStamp, pattern)
    }

    fun getFirstDayOfWeek(): Int {
        val calendarInfo = Date().toCalendar()
        val currentDay = getCurrentDayOfWeek()
        calendarInfo.add(Calendar.DAY_OF_MONTH, -currentDay)
         return calendarInfo[Calendar.DAY_OF_MONTH]
    }

    fun getMonthOfFirstDayOfWeek(): Int {
        val calendarInfo = Date().toCalendar()
        val currentDay = getCurrentDayOfWeek()
        calendarInfo.add(Calendar.DAY_OF_MONTH, -currentDay)
        return calendarInfo[Calendar.MONTH]+1
    }

    fun getYearOfFirstDayOfWeek(): Int {
        val calendarInfo = Date().toCalendar()
        val currentDay = getCurrentDayOfWeek()
        calendarInfo.add(Calendar.DAY_OF_MONTH, -currentDay)
        return calendarInfo[Calendar.YEAR]
    }

    fun getLastDayOfWeek(): Int {
        val calendarInfo = Date().toCalendar()
        val currentDay = getCurrentDayOfWeek()
        calendarInfo.add(Calendar.DAY_OF_MONTH, 6-currentDay)
        return calendarInfo[Calendar.DAY_OF_MONTH]
    }

    fun getFirstDateOfWeek(): String {
        return "${getYearOfFirstDayOfWeek()}/${getMonthOfFirstDayOfWeek()}/${getFirstDayOfWeek()}"
    }

    private fun Date.toCalendar(): Calendar = Calendar.getInstance().also { it.time = this }
}
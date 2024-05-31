package it.unito.progmob.core.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object DateUtils {

    fun formatDate(
        date: LocalDate, format: DateTimeFormat<LocalDate> = LocalDate.Format {
            year()
            char('/')
            monthNumber()
            char('/')
            dayOfMonth()
        }
    ): String {
        return date.format(format)
    }

    fun getCurrentDayOfWeek(): Int {
        val instant = Clock.System.now()
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
        return localDate.value - 1
    }

    fun getCurrentDate(
        format: DateTimeFormat<LocalDate> = LocalDate.Format {
            year()
            char('/')
            monthNumber()
            char('/')
            dayOfMonth()
        }
    ): String {
        val instant = Clock.System.now()
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return formatDate(localDate, format)
    }

    fun getFirstDateOfWeek(): String {
        val currentDay = getCurrentDayOfWeek()
        val instant = Clock.System.now()
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val firstDateOfWeek = localDate.minus(currentDay, DateTimeUnit.DAY)

        return formatDate(firstDateOfWeek)
    }


    fun getLastDateOfWeek(): String {
        val currentDay = getCurrentDayOfWeek()
        val instant = Clock.System.now()
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val lastDateOfWeek = localDate.minus(6 - currentDay, DateTimeUnit.DAY)

        return formatDate(lastDateOfWeek)
    }

}
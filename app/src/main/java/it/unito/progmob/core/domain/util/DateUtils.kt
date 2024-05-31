package it.unito.progmob.core.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateUtils {

    private fun formatDate(
        date: LocalDate,
        format: DateTimeFormat<LocalDate> = LocalDate.Format {
            year()
            char('/')
            monthNumber()
            char('/')
            dayOfMonth()
        }
    ): String = date.format(format)

    fun getCurrentLocalDateTime(): LocalDateTime {
        val instant = Clock.System.now()
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }


    fun getCurrentDateFormatted(
        format: DateTimeFormat<LocalDate> = LocalDate.Format {
            year()
            char('/')
            monthNumber()
            char('/')
            dayOfMonth()
        }
    ): String {
        val localDate = getCurrentLocalDateTime().date
        return formatDate(localDate, format)
    }

    /**
     * Get the instant from now adding or subtracting days
     * @param days the number of days to add or subtract
     * @param operation the operation to perform
     * @return the instant
     */
    fun getInstantFromNow(days: Long, operation: DateOperation ): Instant {
        val instant = Clock.System.now()
        return when(operation){
            DateOperation.PLUS -> instant.plus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
            DateOperation.MINUS -> instant.minus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        }
    }


    fun getCurrentDayOfWeek(): Int {
        val instant = Clock.System.now()
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfWeek
        return localDate.value - 1
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

    enum class DateOperation {
        PLUS,
        MINUS
    }
}
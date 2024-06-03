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

    val defaultFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        year()
        char('/')
        monthNumber()
        char('/')
        dayOfMonth()
    }

    val chartFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }

    /**
     * Format a date [LocalDate] using the formatter provided or the default one if it is not provided
     * @param date the date to format
     * @param formatter the formatter to use
     * @return the formatted date
     */
    private fun formatDate(
        date: LocalDate,
        formatter: DateTimeFormat<LocalDate> = defaultFormatter
    ): String = date.format(formatter)

    /**
     * Get the current date using the kotlin datetime API with the system default timezone
     * @return the current date as a [LocalDateTime]
     */
    private fun getCurrentLocalDateTime(): LocalDateTime {
        val instant = Clock.System.now()
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    /**
     * Get the [LocalDate] from the epoch milliseconds using the kotlin datetime API with the system default timezone
     * @return the [LocalDate] from the epoch milliseconds
     */
    fun getLocalDateFromEpochMillis(dateInMillis: Long): LocalDate =
        Instant.fromEpochMilliseconds(dateInMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date


    /**
     * It turns the date in milliseconds since 1.1.1970 (epoch) into a
     * human readable string based on the formatter provided or the default one if it is not provided
     *
     * @param epochMillis
     * @param formatter the formatter to use
     * @return a human readable string
     */
    fun formatDateFromEpochMillis(
        epochMillis: Long,
        formatter: DateTimeFormat<LocalDate>? = null
    ): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return formatter?.let {
            formatDate(localDate, formatter)
        } ?: formatDate(localDate)
    }

    /**
     * Get the current date formatted based on the formatter provided or the default one if it is not provided
     * @param formatter the formatter to use
     * @return the formatted date
     */
    fun getCurrentDateFormatted(
        formatter: DateTimeFormat<LocalDate>? = null
    ): String {
        val localDate = getCurrentLocalDateTime().date
        return formatter?.let {
            formatDate(localDate, formatter)
        } ?: formatDate(localDate)
    }

    /**
     * Get the instant of a certain date from now adding or subtracting days
     * @param days the number of days to add or subtract
     * @param operation the operation to perform
     * @return the instant
     */
    fun getInstantOfDateFromNow(
        days: Long,
        operation: DateOperation = DateOperation.PLUS
    ): Instant {
        val instant = Clock.System.now()
        return when (operation) {
            DateOperation.PLUS -> instant.plus(
                days,
                DateTimeUnit.DAY,
                TimeZone.currentSystemDefault()
            )

            DateOperation.MINUS -> instant.minus(
                days,
                DateTimeUnit.DAY,
                TimeZone.currentSystemDefault()
            )
        }
    }

    /**
     * Get the current day of the week as an integer (0..6) where 0 is Monday and 6 is Sunday
     * @return the integer between 0 and 6 representing the current day of the week
     */
    fun getCurrentDayOfWeek(): Int {
        val dayOfWeek = getCurrentLocalDateTime().dayOfWeek
        return dayOfWeek.value - 1
    }

    /**
     * Get the first date of the week based on the current date
     * @return the formatted string of the first date of the week
     */
    fun getFirstDateOfWeek(): String {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        val firstDateOfWeek = localDate.minus(currentDay, DateTimeUnit.DAY)
        return formatDate(firstDateOfWeek)
    }

    /**
     * Get the last date of the week based on the current date
     * @return the formatted string of the last date of the week
     */
    fun getLastDateOfWeek(): String {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        val lastDateOfWeek = localDate.minus(6 - currentDay, DateTimeUnit.DAY)
        return formatDate(lastDateOfWeek)
    }

    enum class DateOperation {
        PLUS,
        MINUS
    }
}
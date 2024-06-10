package it.unito.progmob.core.domain.util

import android.content.Context
import it.unito.progmob.core.domain.ext.monthFullNames
import it.unito.progmob.core.domain.ext.weekDaysNames
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

    val monthFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth()
    }

    val standardFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }

    /**
     * Format a date [LocalDate] using the formatter provided or the default one if it is not provided
     *
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
     *
     * @return the current date as a [LocalDateTime]
     */
    private fun getCurrentLocalDateTime(): LocalDateTime {
        val instant = Clock.System.now()
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    /**
     * Get the [LocalDate] from the epoch milliseconds using the kotlin datetime API with the system default timezone
     *
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

    fun formatDateForHistory(
        date: String,
        context: Context,
        parseFormatter: DateTimeFormat<LocalDate>? = null
    ): String {
        val localDate = LocalDate.parse(date, format = parseFormatter ?: defaultFormatter)
        val dayOfWeekStr = context.weekDaysNames[localDate.dayOfWeek.value - 1].lowercase().replaceFirstChar{ it.uppercase() }
        val monthStr = context.monthFullNames[localDate.monthNumber - 1].lowercase().replaceFirstChar{ it.uppercase() }
        val dayStr = localDate.dayOfMonth
        val yearStr = localDate.year
        return "$dayOfWeekStr,  $monthStr $dayStr $yearStr"
    }

    /**
     * Get the current date formatted based on the formatter provided or the default one if it is not provided
     *
     * @param formatter the formatter to use
     * @return the formatted date
     */
    fun formattedCurrentDate(
        formatter: DateTimeFormat<LocalDate>? = null
    ): String {
        val localDate = getCurrentLocalDateTime().date
        return formatDate(localDate, formatter ?: defaultFormatter)
    }


    /**
     * Get the instant of a certain date from now adding or subtracting days
     *
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
     * Get the current month as an integer (1..12) where 1 is January and 12 is December
     *
     * @return the integer between 1 and 12 representing the current month
     */
    fun getCurrentMonth(): Int = getCurrentLocalDateTime().monthNumber


    /**
     * Get the current day of the week as an integer (0..6) where 0 is Monday and 6 is Sunday
     *
     * @return the integer between 0 and 6 representing the current day of the week
     */
    fun getCurrentDayOfWeek(): Int = getCurrentLocalDateTime().dayOfWeek.value - 1


    /**
     * Get the current date
     *
     * @return the [LocalDate] of the current date
     */
    fun getCurrentDayOfWeekFromString(date: String): Int = LocalDate.parse(date, format = defaultFormatter).dayOfWeek.value - 1

    /**
     * Get the first date of the week based on the current date
     *
     * @return the [LocalDate] of the first date of the week
     */
    fun getFirstDateOfWeek(): LocalDate {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        return localDate.minus(currentDay, DateTimeUnit.DAY)
    }

    /**
     * Get the last date of the week based on the current date
     *
     * @return the [LocalDate] of the first date of the week
     */
    fun getLastDateOfWeek(): LocalDate {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        return localDate.plus(6 - currentDay, DateTimeUnit.DAY)

    }

    /**
     * Get the first date of the week based on the current date
     *
     * @return the [LocalDate] of the first date of the week
     */
    fun getFirstDateOfMonth(): LocalDate {
        val localDate = getCurrentLocalDateTime()
        val month = localDate.month
        val year = localDate.year
        return LocalDate(year, month, 1)
    }

    /**
     * Get the last date of the week based on the current date formatted as a string
     *
     * @return the [LocalDate] of the first date of the week
     */
    fun getLastDateOfMonth(): LocalDate {
        val localDate = getCurrentLocalDateTime()
        val maxDayOfMonth = localDate.month.maxLength()
        val month = localDate.month
        val year = localDate.year
        return LocalDate(year, month, maxDayOfMonth)
    }

    /**
     * Get the current year as an integer
     *
     * @return the current year
     */
    fun getCurrentYear(): Int = getCurrentLocalDateTime().year

    /**
     * Get the first date of the week based on the current date formatted as a string
     *
     * @return the formatted string of the first date of the week
     */
    fun formattedFirstDateOfWeek(): String {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        val firstDateOfWeek = localDate.minus(currentDay, DateTimeUnit.DAY)
        return formatDate(firstDateOfWeek)
    }


    /**
     * Get the last date of the week based on the current date formatted as a string
     *
     * @return the formatted string of the last date of the week
     */
    fun formattedLastDateOfWeek(): String {
        val localDate = getCurrentLocalDateTime().date
        val currentDay = getCurrentDayOfWeek()
        val lastDateOfWeek = localDate.plus(6 - currentDay, DateTimeUnit.DAY)
        return formatDate(lastDateOfWeek)
    }

    /**
     * Get the first date of the week based on the current date formatted as a string
     *
     * @return the formatted string of the first date of the week
     */
    fun formattedFirstDateOfMonth(): String {
        val localDate = getCurrentLocalDateTime()
        val month = localDate.month
        val year = localDate.year
        return formatDate(LocalDate(year, month, 1))
    }

    /**
     * Get the last date of the week based on the current date formatted as a string
     *
     * @return the formatted string of the first date of the week
     */
    fun formattedLastDateOfMonth(): String {
        val localDate = getCurrentLocalDateTime()
        val maxDayOfMonth = localDate.month.maxLength()
        val month = localDate.month
        val year = localDate.year
        return formatDate(LocalDate(year, month, maxDayOfMonth))
    }


    /**
     * Get the current year formatted as a string
     *
     * @return the formatted string of the current year
     */
    fun formattedCurrentYear(): String {
        val localDate = getCurrentLocalDateTime()
        return localDate.year.toString()
    }

    enum class DateOperation {
        PLUS,
        MINUS
    }
}
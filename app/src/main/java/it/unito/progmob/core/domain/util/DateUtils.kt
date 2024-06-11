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

    /**
     * The default formatter for displaying dates in the format "yyyy/MM/dd".
     */
    val defaultFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        year()
        char('/')
        monthNumber()
        char('/')
        dayOfMonth()
    }

    /**
     * A formatter for displaying only the day of the month from a [LocalDate].
     */
    val monthFormatter: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth()
    }

    /**
     * A formatter for displaying dates in the format "dd/MM/yyyy".
     */
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
    fun getCurrentLocalDateTime(): LocalDateTime {
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

    /**
     * Extract the strings components of a date, where this date is provided as the milliseconds
     * since epoch
     *
     * @param epochMillis the date to format as milliseconds since epoch
     * @param monthsNames the array of months names
     * @return an array of strings containing the month, day and year. Null if the epochMillis date is null
     */
    fun extractDateComponentsFromEpochMillis(
        epochMillis: Long?,
        monthsNames: Array<String>
    ): Array<String>? {
        return epochMillis?.let{
            val instant = Instant.fromEpochMilliseconds(epochMillis)
            val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val monthStr = monthsNames[localDate.monthNumber - 1]
            val dayStr = localDate.dayOfMonth.toString()
            val yearStr = localDate.year.toString()
            arrayOf(monthStr, dayStr, yearStr)
        }
    }

    /**
     * Format a date provided as a string into a more expressive format (e.g., "Tue, January 1 2024")
     *
     * @param date the date to format
     * @param weekDaysNames the array of week days names
     * @param monthsNames the array of months names
     * @param parseFormatter the formatter to use
     * @return the formatted date in a more expressive format
     */
    fun formatDateFromStringExpanded(
        date: String,
        weekDaysNames: Array<String>,
        monthsNames: Array<String>,
        parseFormatter: DateTimeFormat<LocalDate>? = null
    ): String {
        val localDate = LocalDate.parse(date, format = parseFormatter ?: defaultFormatter)
        val dayOfWeekStr = weekDaysNames[localDate.dayOfWeek.value - 1]
        val monthStr = monthsNames[localDate.monthNumber - 1]
        val dayStr = localDate.dayOfMonth
        val yearStr = localDate.year
        return "$dayOfWeekStr,  $monthStr $dayStr $yearStr"
    }

    /**
     * Get the current date formatted based on the formatter provided or the default one if it is
     * not provided
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
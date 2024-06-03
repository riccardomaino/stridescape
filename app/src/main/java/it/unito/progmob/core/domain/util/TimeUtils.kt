package it.unito.progmob.core.domain.util


import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {

    private val defaultFormatter: DateTimeFormat<LocalTime> = LocalTime.Format {
        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    private fun formatTime(
        time: LocalTime,
        formatter: DateTimeFormat<LocalTime> = defaultFormatter
    ): String = time.format(formatter)

    /**
     * It turns the time in milliseconds since 1.1.1970 (epoch) into a
     * human readable string
     *
     * @param epochSeconds
     * @return a human readable string
     */
    fun formatTimeFromEpochSeconds(
        epochSeconds: Long,
        formatter: DateTimeFormat<LocalTime>? = null
    ): String {
        val instant = Instant.fromEpochSeconds(epochSeconds)
        val localTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time
        return formatter?.let {
            formatTime(localTime, formatter)
        } ?: formatTime(localTime)
    }

    /**
     * It turns the time in milliseconds into a human readable string (e.g. the HH:mm:ss format)
     *
     * @param timeInMillis the time in milliseconds
     * @return a human readable string
     */
    fun formatMillisTime(timeInMillis: Long): String {
//        val instant = Instant.fromEpochMilliseconds(timeInMillis)
//        val localTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time
//        return formatter?.let {
//            formatTime(localTime, formatter)
//        } ?: formatTime(localTime)
        val duration: Duration = timeInMillis.toDuration(DurationUnit.MILLISECONDS)
        return duration.toComponents { hours, minutes, seconds, _ ->
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        }
    }

    /**
     * It converts the time in milliseconds into minutes and rounds it to the nearest minute value
     * (e.g. 30 seconds are rounded to 1 minute)
     *
     * @param timeInMillis the time in milliseconds
     * @return the time in minutes
     */
    fun convertMillisToMinutes(timeInMillis: Long): Int {
        val duration: Duration = timeInMillis.toDuration(DurationUnit.MILLISECONDS)
        val mins = duration.inWholeMinutes.toInt()
        val secs = duration.minus(mins.toDuration(DurationUnit.MINUTES)).inWholeSeconds
        return mins + if (secs >= 30) 1 else 0
    }

    /**
     * It turns the time in milliseconds into the HH:mm format
     *
     * @param timeInMillis the time in milliseconds
     * @return a human readable string
     */
    fun formatMillisTimeHoursMinutes(timeInMillis: Long): String {
        val duration: Duration = timeInMillis.toDuration(DurationUnit.MILLISECONDS)
        return duration.toComponents { hours, minutes, _, _ ->
            "%02d:%02d".format(hours, minutes)
        }
    }
}
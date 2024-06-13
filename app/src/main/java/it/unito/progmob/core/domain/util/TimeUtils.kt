package it.unito.progmob.core.domain.util


import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {

    /**
     * It turns the time in milliseconds into a human readable string (e.g. the HH:mm:ss format)
     *
     * @param timeInMillis the time in milliseconds
     * @return a human readable string
     */
    fun formatMillisTime(timeInMillis: Long): String {
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
        val minutes = duration.inWholeMinutes.toInt()
        val secs = duration.minus(minutes.toDuration(DurationUnit.MINUTES)).inWholeSeconds
        return minutes + if (secs >= 30) 1 else 0
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
            "%2dh%02dm".format(hours, minutes)
        }
    }
}
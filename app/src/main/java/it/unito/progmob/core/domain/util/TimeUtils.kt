package it.unito.progmob.core.domain.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {
    /**
     * It turns the time in milliseconds since 1.1.1970 (epoch) into a
     * human readable string
     *
     * @param epochSeconds
     * @param pattern the formatter of the string, e.g. "dd MMMM, HH:mm:ss"
     * @return a human readable string
     */
    fun formatEpochTime(epochSeconds: Long, pattern: String): String {
        // Create an Instant from the epoch seconds
        val instant = Instant.ofEpochSecond(epochSeconds)
        // Define a formatter
        val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
        // Format the Instant
        return formatter.format(instant)
    }

    /**
     * It turns the time in milliseconds into the HH:mm:ss format
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
}
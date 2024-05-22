package it.unito.progmob.core.domain.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
     * It turns the time in milliseconds into a human readable string
     *
     * @param timeInMillis
     * @param pattern the formatter of the string, e.g. "dd MMMM, HH:mm:ss"
     * @return a human readable string
     */
    fun formatMillisTime(timeInMillis: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(timeInMillis)
    }
}
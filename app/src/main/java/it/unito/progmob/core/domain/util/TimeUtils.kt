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
    /**
     * It turns the time in milliseconds since 1.1.1970 (epoch) into a
     * human readable string
     *
     * @param epochSeconds
     * @param format the formatter of the string
     * @return a human readable string
     */
    fun formatEpochSeconds(
        epochSeconds: Long,
        format: DateTimeFormat<LocalTime> = LocalTime.Format {
            hour()
            char(':')
            minute()
            char(':')
            second()
        }
    ): String = Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(TimeZone.currentSystemDefault()).time.format(format)

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
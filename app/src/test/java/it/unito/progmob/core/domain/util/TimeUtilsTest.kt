package it.unito.progmob.core.domain.util

import org.junit.Assert.assertTrue
import org.junit.Test

class TimeUtilsTest {
    @Test
    fun `format the time in milliseconds to a human readable string, should match the 00 00 00`() {
        val formattedTime = TimeUtils.formatMillisTime(0)
        assertTrue(formattedTime == "00:00:00")
    }

    @Test
    fun `convert the time in milliseconds to minutes, should match 1`() {
        val minutes = TimeUtils.convertMillisToMinutes(60000)
        assertTrue(minutes == 1)
    }

    @Test
    fun `convert the time in milliseconds to hours and minutes, should match 1h30m`() {
        val formattedTime = TimeUtils.formatMillisTimeHoursMinutes(5400000)
        assertTrue(formattedTime == " 1h30m")
    }
}
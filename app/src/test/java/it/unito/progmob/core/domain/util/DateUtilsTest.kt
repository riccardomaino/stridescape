package it.unito.progmob.core.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `get the current formatted with the default formatter, should match the regex`() {
        val formattedCurrentDate = DateUtils.formattedCurrentDate()
        assertTrue(formattedCurrentDate.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `get the current formatted with the custom formatter, should match the regex`() {
        val formattedCurrentDate = DateUtils.formattedCurrentDate(DateUtils.standardFormatter)
        assertTrue(formattedCurrentDate.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `get the current day of the week, should be between 0 and 6`() {
        val currentDayOfWeek = DateUtils.getCurrentDayOfWeek()
        assertThat(currentDayOfWeek in 1..7).isTrue()
    }

    @Test
    fun `get the current month, should be between 1 and 12`() {
        val currentMonth = DateUtils.getCurrentMonth()
        assertThat(currentMonth in 1..12).isTrue()
    }

    @Test
    fun `get the current year, should be greater than 2020`() {
        val currentYear = DateUtils.getCurrentYear()
        assertThat(currentYear > 2020).isTrue()
    }

    @Test
    fun `get the current date, should be between 1 and 31`() {
        val currentDay = DateUtils.getCurrentLocalDateTime()
        print(currentDay)
    }

    @Test
    fun `format the date from epoch milliseconds to a string, should match the 01 02 2021`() {
        val formattedDate = DateUtils.formatDateFromEpochMillis(1612137600000, DateUtils.standardFormatter)
        assertEquals("01/02/2021", formattedDate)
    }

    @Test
    fun `extract the date components from epoch milliseconds, should return 01 02 2021`() {
        val monthsNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val dateComponents = DateUtils.extractDateComponentsFromEpochMillis(1612137600000, monthsNames)
        assertEquals("February", dateComponents?.get(0))
        assertEquals("1", dateComponents?.get(1))
        assertEquals("2021", dateComponents?.get(2))
    }

    @Test
    fun `format date from string expanded to string, should match 'Wed, June 12 2024'`() {
        val monthsNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val weekDaysNames = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val formattedDate = DateUtils.formatDateFromStringExpanded("2024/06/12",weekDaysNames, monthsNames, DateUtils.defaultFormatter)
        assertEquals("Wed, June 12 2024", formattedDate)
    }

    @Test
    fun `get the formatted current date with default formatter, should match the regex`() {
        val formattedCurrentDate = DateUtils.formattedCurrentDate(DateUtils.defaultFormatter)
        assertTrue(formattedCurrentDate.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `get the formatted current date with custom formatter, should match the regex`() {
        val formattedCurrentDate = DateUtils.formattedCurrentDate(DateUtils.standardFormatter)
        assertTrue(formattedCurrentDate.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `get the current day of week from string, should be between 0 and 6`() {
        val currentDayOfWeek = DateUtils.getCurrentDayOfWeekFromString("2024/06/12")
        assertThat(currentDayOfWeek in 0..6).isTrue()
    }

    @Test
    fun `get the last date of week, should match the regex`() {
        val formattedLastDateOfWeek = DateUtils.formattedLastDateOfWeek()
        assertTrue(formattedLastDateOfWeek.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `get the last date of month, should match the regex`() {
        val formattedLastDateOfMonth = DateUtils.formattedLastDateOfMonth()
        assertTrue(formattedLastDateOfMonth.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `the formatted first date of week, should match the regex`() {
        val formattedFirstDayOfWeek = DateUtils.formattedFirstDateOfWeek()
        assertTrue(formattedFirstDayOfWeek.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `the formatted last date of week, should match the regex`() {
        val formattedLastDateOfWeek = DateUtils.formattedLastDateOfWeek()
        assertTrue(formattedLastDateOfWeek.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `the formatted first date of month, should match the regex`() {
        val formattedFirstDayOfMonth = DateUtils.formattedFirstDateOfMonth()
        assertTrue(formattedFirstDayOfMonth.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `the formatted last date of month, should match the regex`() {
        val formattedLastDateOfMonth = DateUtils.formattedLastDateOfMonth()
        assertTrue(formattedLastDateOfMonth.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
    }

    @Test
    fun `the formatted current year, should match the regex`() {
        val formattedCurrentYear = DateUtils.formattedCurrentYear()
        assertTrue(formattedCurrentYear.matches(Regex("\\d{4}")))
    }
}
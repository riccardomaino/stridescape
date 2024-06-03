package it.unito.progmob.stats.domain.iterator

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class LocalDateIterator(
    val startDate: LocalDate,
    val endDateInclusive: LocalDate,
    val stepsDays: Long
): Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext(): Boolean = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plus(stepsDays, DateTimeUnit.DAY)
        return next
    }

}
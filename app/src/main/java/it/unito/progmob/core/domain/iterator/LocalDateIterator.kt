package it.unito.progmob.core.domain.iterator

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * An iterator that allows you to iterate over a range of [LocalDate]s.
 *
 * @param startDate The start date of the iteration (inclusive).
 * @param endDateInclusive The end date of the iteration (inclusive).
 * @param stepsDays The number of days to advance on each iteration.
 */
class LocalDateIterator(
    private val startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepsDays: Long
): Iterator<LocalDate> {
    private var currentDate = startDate

    /**
     * Returns `true` if the iteration has more elements.
     */
    override fun hasNext(): Boolean = currentDate <= endDateInclusive

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plus(stepsDays, DateTimeUnit.DAY)
        return next
    }
}
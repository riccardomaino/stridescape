package it.unito.progmob.stats.domain.iterator

import kotlinx.datetime.LocalDate

/**
 * A progression of dates.
 *
 * Creates a progression of dates from the specified [start] date to the specified [endInclusive] date,
 * with the specified [stepDays] (default 1).
 *
 * @param start The first date in the progression.
 * @param endInclusive The last date in the progression.
 * @param stepDays The number of days between each date in the progression.
 */
class LocalDateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
): Iterable<LocalDate>, ClosedRange<LocalDate> {
    /**
     * Returns an iterator over the elements of this progression.
     */
    override fun iterator(): Iterator<LocalDate> = LocalDateIterator(start, endInclusive, stepDays)
    /**
     * Creates a new progression with the given [days] as the step.
     */
    infix fun step(days: Long) = LocalDateProgression(start, endInclusive,days)
}

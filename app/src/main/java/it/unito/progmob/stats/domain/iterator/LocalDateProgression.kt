package it.unito.progmob.stats.domain.iterator

import kotlinx.datetime.LocalDate

class LocalDateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
): Iterable<LocalDate>, ClosedRange<LocalDate> {
    override fun iterator(): Iterator<LocalDate> = LocalDateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = LocalDateProgression(start, endInclusive, days)
}

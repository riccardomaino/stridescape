package it.unito.progmob.core.domain.ext

import it.unito.progmob.core.domain.iterator.LocalDateProgression
import kotlinx.datetime.LocalDate

/**
 * Creates a [LocalDateProgression] from this value to the specified [other] value.
 */
operator fun LocalDate.rangeTo(other: LocalDate): LocalDateProgression = LocalDateProgression(this, other)
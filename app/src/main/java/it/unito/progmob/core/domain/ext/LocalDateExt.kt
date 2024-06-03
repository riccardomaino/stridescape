package it.unito.progmob.core.domain.ext

import it.unito.progmob.stats.domain.iterator.LocalDateProgression
import kotlinx.datetime.LocalDate

operator fun LocalDate.rangeTo(other: LocalDate): LocalDateProgression = LocalDateProgression(this, other)
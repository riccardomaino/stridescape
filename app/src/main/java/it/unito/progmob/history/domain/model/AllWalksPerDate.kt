package it.unito.progmob.history.domain.model

import androidx.compose.runtime.Immutable

/**
 * Data class representing all walks associated with a specific date.
 *
 * @param date The date in the format "yyyy-MM-dd".
 * @param walks A mutable list of [WalkWithPathPoints] objects representing the walks that took
 * place on the specified date.
 */
@Immutable
data class AllWalksPerDate(
    val date: String,
    val walks: MutableList<WalkWithPathPoints>
)
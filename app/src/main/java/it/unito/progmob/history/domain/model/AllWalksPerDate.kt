package it.unito.progmob.history.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AllWalksPerDate(
    val date: String,
    val walks: MutableList<WalkWithPathPoints>
)
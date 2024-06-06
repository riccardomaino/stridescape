package it.unito.progmob.history.domain.model

import it.unito.progmob.core.domain.model.WalkWithPathPoints

data class AllWalksPerDate(
    val date: String,
    val walks: MutableList<WalkWithPathPoints>
)
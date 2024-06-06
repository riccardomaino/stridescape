package it.unito.progmob.history.domain.model

data class AllWalksPerDate(
    val date: String,
    val walks: MutableList<WalkWithPathPoints>
)
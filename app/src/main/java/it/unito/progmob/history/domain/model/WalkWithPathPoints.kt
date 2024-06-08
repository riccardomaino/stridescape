package it.unito.progmob.history.domain.model

import it.unito.progmob.tracking.domain.model.PathPoint

data class WalkWithPathPoints(
    val walkId: Int,
    val weekDay: Int,
    val date: String,
    val month: Int,
    val steps: Int,
    val distance: Int,
    val time: Long,
    val calories: Int,
    val averageSpeed: Float,
    val pathPoints: List<PathPoint>
)
package it.unito.progmob.core.domain.state

import it.unito.progmob.core.domain.model.PathPoint

data class WalkState(
    val isTracking: Boolean = false,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val speedInKMH : Float = 0f,
    val pathPoints: List<PathPoint> = emptyList()
)

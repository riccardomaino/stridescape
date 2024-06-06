package it.unito.progmob.tracking.presentation.state

import it.unito.progmob.tracking.domain.model.PathPoint

data class UiTrackingState(
    val isTrackingStarted: Boolean = false,
    val isTracking: Boolean = false,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val speedInKMH : Float = 0f,
    val pathPoints: List<PathPoint> = emptyList(),
    val steps: Int = 0,
    val caloriesBurnt: Int = 0
)

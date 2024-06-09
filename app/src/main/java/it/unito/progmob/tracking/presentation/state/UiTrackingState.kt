package it.unito.progmob.tracking.presentation.state

import it.unito.progmob.tracking.domain.model.PathPoint

/**
 * Data class representing the current state of the UI tracking feature.
 *
 * @property isTrackingStarted Indicates whether the tracking has been started.
 * @property isTracking Indicates whether the tracking is currently active.
 * @property distanceInMeters The total distance covered in meters.
 * @property timeInMillis The total time elapsed in milliseconds.
 * @property speedInKMH The current speed in kilometers per hour.
 * @property pathPoints A list of [PathPoint]s representing the tracked path.
 * @property steps The number of steps taken.
 * @property caloriesBurnt The estimated number of calories burned.
 */
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

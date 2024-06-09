package it.unito.progmob.tracking.domain.model

/**
 * Data class representing a walk.
 *
 * @property isTrackingStarted Indicates whether the walk tracking has been started.
 * @property isTracking Indicates whether the walk is currently being tracked.
 * @property distanceInMeters The total distance of the walk in meters.
 * @property timeInMillis The total duration of the walk in milliseconds.
 * @property speedInKMH The current speed of the walk in kilometers per hour.
 * @property steps The number of steps taken during the walk.
 * @property pathPoints A list of [PathPoint]s representing the path of the walk.
 */
data class Walk(
    val isTrackingStarted: Boolean = false,
    val isTracking: Boolean = false,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val speedInKMH : Float = 0f,
    val steps: Int = 0,
    val pathPoints: List<PathPoint> = emptyList()
)

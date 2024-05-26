package it.unito.progmob.tracking.domain.model

data class Walk(
    val isTracking: Boolean = false,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val speedInKMH : Float = 0f,
    val steps: Int = 0,
    val pathPoints: List<PathPoint> = emptyList()
)

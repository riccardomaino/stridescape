package it.unito.progmob.tracking.domain.model

import kotlinx.serialization.Serializable

/**
 * Sealed interface that represents a point in a path.
 */
@Serializable
sealed interface PathPoint {
    /**
     * A point with location data.
     *
     * @property lat The latitude of the point.
     * @property lng The longitude of the point.
     * @property speed The speed at the point.
     */
    @Serializable
    data class LocationPoint(val lat: Double, val lng: Double, val speed: Float) : PathPoint

    /**
     * An empty point, representing no data.
     */
    @Serializable
    data object EmptyPoint : PathPoint
}

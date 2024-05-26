package it.unito.progmob.tracking.domain.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
sealed interface PathPoint {
    @Serializable
    data class LocationPoint(val lat: Double, val lng: Double, val speed: Float) : PathPoint
    @Serializable
    data object EmptyPoint : PathPoint
}

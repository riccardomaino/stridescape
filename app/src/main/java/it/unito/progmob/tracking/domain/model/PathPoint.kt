package it.unito.progmob.tracking.domain.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
sealed interface PathPoint {
    data class LocationPoint(val latLng: LatLng, val speed: Float) : PathPoint
    data object EmptyPoint : PathPoint
}

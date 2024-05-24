package it.unito.progmob.core.domain.model

import com.google.android.gms.maps.model.LatLng

sealed interface PathPoint {
    data class LocationPoint(val latLng: LatLng, val speed: Float) : PathPoint
    data object EmptyPoint : PathPoint
}

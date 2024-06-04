package it.unito.progmob.tracking.domain.manager

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTrackingManager {
    fun trackSingleLocation(onSuccess: (latitude: Double, longitude: Double) -> Unit): Unit
    fun startTrackingLocation(intervalMillis: Long): Flow<Location>
}
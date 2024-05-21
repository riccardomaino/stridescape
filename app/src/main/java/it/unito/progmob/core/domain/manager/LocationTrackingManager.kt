package it.unito.progmob.core.domain.manager

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTrackingManager {
    fun trackSingleLocation(onSuccess: (latitude: String, longitude: String) -> Unit)
    fun trackLocation(intervalMillis: Long): Flow<Location>
}
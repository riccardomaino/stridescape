package it.unito.progmob.core.domain.manager

import android.location.Location
import com.google.android.gms.location.LocationCallback
import kotlinx.coroutines.flow.Flow

interface LocationTrackingManager {
    fun trackSingleLocation(onSuccess: (latitude: String, longitude: String) -> Unit)
    fun startTrackingLocation(intervalMillis: Long): Flow<Location>
    fun stopTrackingLocation()
}
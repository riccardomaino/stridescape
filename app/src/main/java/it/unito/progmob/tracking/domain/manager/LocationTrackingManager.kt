package it.unito.progmob.tracking.domain.manager

import android.location.Location
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.location.LocationSettingsResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the location tracking manager.
 */
interface LocationTrackingManager {
    fun isConnected(): Boolean
    fun checkLocationSettings(onDisabled: (IntentSenderRequest) -> Unit, onEnabled: (LocationSettingsResponse) -> Unit)
    fun trackSingleLocation(onSuccess: (latitude: Double, longitude: Double) -> Unit)
    fun startTrackingLocation(intervalMillis: Long): Flow<Location>
}
package it.unito.progmob.tracking.data.manager

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationSettingsResponse
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class FakeLocationTrackingManager @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationTrackingManager {

    override fun isConnected(): Boolean {
        return true
    }

    override fun checkLocationSettings(
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: (LocationSettingsResponse) -> Unit
    ) {}

    override fun trackSingleLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit
    ) {
        onSuccess(1.0, 1.0)
    }

    override fun startTrackingLocation(intervalMillis: Long): Flow<Location> {
        val locationList = listOf(
            Location("gps").apply {
                latitude = 1.0
                longitude = 1.0
            },
            Location("gps").apply {
                latitude = 2.0
                longitude = 2.0
            },
            Location("gps").apply {
                latitude = 3.0
                longitude = 3.0
            }
        )
        return locationList.asFlow()
    }
}
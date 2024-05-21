package it.unito.progmob.core.data.manager

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Implementation of the [LocationTrackingManager] interface that uses the Fused Location Provider API to
 * track the user's location.
 *
 * @param fusedLocationClient The Fused Location Provider Client.
 */
@SuppressLint("MissingPermission")
class LocationTrackingManagerImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationTrackingManager {

    /**
     * Tracks the user's single location and returns the latitude and longitude as strings.
     *
     * @param onSuccess A callback that receives the latitude and longitude as strings.
     */
    override fun trackSingleLocation(
        onSuccess: (latitude: String, longitude: String) -> Unit
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val latitude = location.latitude.toString()
            val longitude = location.longitude.toString()
            onSuccess(latitude, longitude)
        }
    }

    /**
     * Tracks the user's location and returns a Flow of [Location] objects. It creates an anonymous
     * [LocationCallback] that will be used retrieve the location by the [FusedLocationProviderClient].
     *
     * @param intervalMillis The interval in milliseconds at which to track the location.
     * @return A Flow of [Location] objects.
     */
    override fun trackLocation(intervalMillis: Long): Flow<Location> {
        return callbackFlow {

            val locationCallback = object: LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.locations.lastOrNull()?.let { location ->
                        launch {
                            send(location)
                        }
                    }
                }
            }


            val request = LocationRequest.Builder(intervalMillis)
                .setMinUpdateIntervalMillis(intervalMillis)
                .build()

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}
package it.unito.progmob.tracking.data.manager

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Implementation of the [LocationTrackingManager] interface that uses the Fused Location Provider
 * API to track the user's location based on the Network and GPS providers
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
     * @param onSuccess A callback that receives the latitude and longitude as double.
     */
    override fun trackSingleLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                onSuccess(latitude, longitude)
            }
        }
    }

    /**
     * Tracks the user's location and returns a Flow of [Location] objects. It creates an anonymous
     * [LocationCallback] that will be used to retrieve the location thanks to the [FusedLocationProviderClient].
     *
     * @param intervalMillis The interval in milliseconds at which to track the location.
     * @return A Flow of [Location] objects.
     */
    override fun startTrackingLocation(intervalMillis: Long): Flow<Location> {
        return callbackFlow {
            // Defining the LocationCallback used to retrieve the user's location.
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.locations.lastOrNull()?.let { location ->
                        launch {
                            send(location)
                            Log.d(
                                TAG,
                                "LocationCallback: sending new location (${location.latitude}, ${location.longitude})"
                            )
                        }
                    }
                }
            }

            val request = LocationRequest.Builder(intervalMillis)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(intervalMillis)
                .build()

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                Log.d(TAG, "awaitClose: removing the LocationCallback")
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    companion object {
        private val TAG = LocationTrackingManagerImpl::class.java.simpleName.toString()
    }
}
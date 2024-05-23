package it.unito.progmob.core.data.manager

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
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
     * The reference to the LocationCallback used to retrieve the user's location. We save the
     * reference in order to stop tracking the user's location by removing the callback from the
     * [FusedLocationProviderClient].
     */
    private var locationCallback: LocationCallback? = null

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
    override fun startTrackingLocation(intervalMillis: Long): Flow<Location> {
        return callbackFlow {
            if(locationCallback == null){
                Log.d(TAG, "Creating the first time the LocationCallback")
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        Log.d(TAG, "Before sending to Flow ${locationResult.locations}")
                        locationResult.locations.lastOrNull()?.let { location ->
                            launch {
                                send(location)
                                Log.d(TAG, "After sending to Flow: ${location.latitude}, ${location.longitude}")
                            }
                        }
                    }
                }
            }

            val request = LocationRequest.Builder(intervalMillis)
                .setMinUpdateIntervalMillis(intervalMillis)
                .build()

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback!!,
                Looper.getMainLooper()
            )

            awaitClose {
                locationCallback?.let {
                    fusedLocationClient.removeLocationUpdates(it)
                }
                locationCallback = null
            }
        }
    }

/**
     * Stops tracking the user's location by removing the callback from the [FusedLocationProviderClient].
     */
    override fun stopTrackingLocation() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    companion object{
        private val TAG = LocationTrackingManagerImpl::class.java.simpleName.toString()
    }
}
package it.unito.progmob.tracking.data.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import it.unito.progmob.core.domain.Constants
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
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationTrackingManager {

    /**
     * Checks if the device has the gps provider enabled.
     *
     * @return True if the device has the gps provider enabled, false otherwise.
     */
    override fun isConnected(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Checks if the location settings can satisfy the request. If the location settings can satisfy
     * the request, the [onEnabled] callback will be called. If the location settings cannot satisfy
     * the request, the [onDisabled] callback will be called.
     *
     * @param onDisabled A callback that receives an [IntentSenderRequest] object.
     * @param onEnabled A callback that receives a [LocationSettingsResponse] object.
     */
    override fun checkLocationSettings(
        onDisabled: (IntentSenderRequest) -> Unit,
        onEnabled: (LocationSettingsResponse) -> Unit
    ) {
        // Create a LocationRequest object used to verify if the location settings can satisfy the request
        val locationRequest = LocationRequest.Builder(Constants.LOCATION_TRACKING_INTERVAL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(Constants.LOCATION_TRACKING_INTERVAL)
            .build()

        // Create a LocationSettingsRequest object used to verify if the location settings
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(context)

        // Create a Task object that will be used to verify if the location settings can satisfy the request
        val locationSettingsTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        // Add a listener to the Task object that will be called when the Task completes successfully.
        // The onEnabled() callback will be called
        locationSettingsTask.addOnSuccessListener { locationSettingsResponse ->
            onEnabled(locationSettingsResponse)
        }

        // Add a listener to the Task object that will be called when the Task fails. The
        // The onDisabled() callback will be called
        locationSettingsTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }


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
     * [LocationCallback] that will be used to retrieve the location thanks to the
     * [FusedLocationProviderClient]. The [LocationCallback] will be added to the
     * [FusedLocationProviderClient] and will be removed when the Flow is closed.
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
                        }
                    }
                }
            }

            val locationRequest = LocationRequest.Builder(intervalMillis)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(intervalMillis)
                .build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}
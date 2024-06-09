package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import javax.inject.Inject

/**
 * Use case for tracking a single location update.
 *
 * @param locationTrackingManager The manager responsible for tracking location updates.
 */
class TrackSingleLocationUseCase @Inject constructor(
    private val locationTrackingManager: LocationTrackingManager
) {
    /**
     * Tracks a single location update and invokes the [onSuccess] callback with the latitude and
     * longitude of the location.
     *
     * @param onSuccess Callback to be invoked when the location is successfully retrieved.
     */
    operator fun invoke(onSuccess: (latitude: Double, longitude: Double) -> Unit){
        locationTrackingManager.trackSingleLocation(onSuccess = onSuccess)
    }
}
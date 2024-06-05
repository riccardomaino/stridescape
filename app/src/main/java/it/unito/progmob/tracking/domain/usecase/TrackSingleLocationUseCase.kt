package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import javax.inject.Inject

class TrackSingleLocationUseCase @Inject constructor(
    private val locationTrackingManager: LocationTrackingManager
) {
    operator fun invoke(onSuccess: (latitude: Double, longitude: Double) -> Unit){
        locationTrackingManager.trackSingleLocation(onSuccess = onSuccess)
    }
}
package it.unito.progmob.tracking.domain.usecase

import com.google.android.gms.maps.model.LatLng
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import javax.inject.Inject

class TrackSingleLocationUseCase @Inject constructor(
    private val locationTrackingManager: LocationTrackingManager
) {
    operator fun invoke() {
        locationTrackingManager.trackSingleLocation(onSuccess = { latitude, longitude ->
            LatLng(latitude, longitude)
        })
    }
}
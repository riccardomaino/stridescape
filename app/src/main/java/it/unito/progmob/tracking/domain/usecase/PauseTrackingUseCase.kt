package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import javax.inject.Inject

/**
 * Use case for pausing the tracking service.
 *
 * @param trackingServiceManager The manager responsible for controlling the tracking service.
 */
class PauseTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    /**
     * Pauses the tracking service.
     */
    operator fun invoke(){
        trackingServiceManager.pauseTrackingService()
    }
}
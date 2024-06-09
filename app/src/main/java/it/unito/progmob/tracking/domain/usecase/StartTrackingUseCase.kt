package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import javax.inject.Inject

/**
 * Use case for starting the tracking service.
 *
 * @param trackingServiceManager The manager responsible for controlling the tracking service.
 */
class StartTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    /**
     * Starts the tracking service.
     */
    operator fun invoke(){
        trackingServiceManager.startTrackingService()
    }
}
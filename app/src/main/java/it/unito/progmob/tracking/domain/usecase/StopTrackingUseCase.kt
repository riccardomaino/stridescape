package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import javax.inject.Inject

/**
 * Use case for stopping the tracking service.
 *
 * @param trackingServiceManager The manager responsible for controlling the tracking service.
 */
class StopTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    /**
     * Stops the tracking service.
     */
    operator fun invoke(){
        trackingServiceManager.stopTrackingService()
    }
}
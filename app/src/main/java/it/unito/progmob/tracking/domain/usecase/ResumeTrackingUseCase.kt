package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import javax.inject.Inject

/**
 * Use case for resuming the tracking service.
 *
 * @param trackingServiceManager The manager responsible for controlling the tracking service.
 */
class ResumeTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    /**
     * Resumes the tracking service.
     */
    operator fun invoke(){
        trackingServiceManager.resumeTrackingService()
    }
}
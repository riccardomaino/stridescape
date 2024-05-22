package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.manager.TrackingServiceManager
import javax.inject.Inject

class PauseTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    operator fun invoke(){
        trackingServiceManager.pauseTrackingService()
    }
}
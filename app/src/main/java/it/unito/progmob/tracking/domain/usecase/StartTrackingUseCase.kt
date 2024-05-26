package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import javax.inject.Inject

class StartTrackingUseCase @Inject constructor(
    private val trackingServiceManager: TrackingServiceManager
){
    operator fun invoke(){
        trackingServiceManager.startTrackingService()
    }
}
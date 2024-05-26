package it.unito.progmob.tracking.domain.manager

interface TrackingServiceManager {
    fun startTrackingService()

    fun stopTrackingService()

    fun resumeTrackingService()

    fun pauseTrackingService()
}
package it.unito.progmob.core.domain.manager

interface TrackingServiceManager {
    fun startTrackingService()

    fun stopTrackingService()

    fun resumeTrackingService()

    fun pauseTrackingService()
}
package it.unito.progmob.tracking.domain.manager

/**
 * Interface for the tracking service manager.
 */
interface TrackingServiceManager {
    fun startTrackingService()

    fun stopTrackingService()

    fun resumeTrackingService()

    fun pauseTrackingService()
}
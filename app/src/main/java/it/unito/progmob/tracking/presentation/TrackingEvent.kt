package it.unito.progmob.tracking.presentation

import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.location.LocationSettingsResponse

/**
 * Sealed class representing different tracking events.
 */
sealed class TrackingEvent {
    /**
     * Event to start the tracking service.
     */
    data object StartTrackingService : TrackingEvent()

    /**
     * Event to resume the tracking service.
     */
    data object ResumeTrackingService : TrackingEvent()

    /**
     * Event to pause the tracking service.
     */
    data object PauseTrackingService : TrackingEvent()

    /**
     * Event to stop the tracking service.
     */
    data object StopTrackingService : TrackingEvent()

    /**
     * Event to track a single location.
     */
    data object TrackSingleLocation : TrackingEvent()

    /**
     * Event to show or hide the stop walk dialog.
     *
     * @param showDialog True to show the dialog, false to hide it.
     */
    data class ShowStopWalkDialog(val showDialog: Boolean) : TrackingEvent()

    /**
     * Event to check the location settings.
     *
     * @param onDisabled Callback to be invoked if location settings are disabled.
     * @param onEnabled Callback to be invoked if location settings are enabled.
     */
    data class CheckLocationSettings(
        val onDisabled: (IntentSenderRequest) -> Unit,
        val onEnabled: (LocationSettingsResponse) -> Unit
    ) : TrackingEvent()

    /**
     * Event to update the location enabled status.
     *
     * @param status True if location is enabled, false otherwise.
     */
    data class UpdateIsLocationEnabledStatus(val status: Boolean) : TrackingEvent()


    /**
     * Event to resume collecting thw walk state
     */
    data object StartCollectingState : TrackingEvent()

    /**
     * Event to stop collecting the walk state
     */
    data object StopCollectingState : TrackingEvent()
}
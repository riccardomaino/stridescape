package it.unito.progmob.tracking.presentation

import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.location.LocationSettingsResponse


sealed class TrackingEvent {
    data object StartTrackingService : TrackingEvent()
    data object ResumeTrackingService : TrackingEvent()
    data object PauseTrackingService : TrackingEvent()
    data object StopTrackingService : TrackingEvent()
    data object TrackSingleLocation : TrackingEvent()
    data class ShowStopWalkDialog(val showDialog: Boolean) : TrackingEvent()
    data class CheckLocationSettings(
        val onDisabled: (IntentSenderRequest) -> Unit,
        val onEnabled: (LocationSettingsResponse) -> Unit
    ) : TrackingEvent()

    data class UpdateIsLocationEnabledStatus(val status: Boolean) : TrackingEvent()
}
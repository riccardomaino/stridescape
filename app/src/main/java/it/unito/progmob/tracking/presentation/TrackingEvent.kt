package it.unito.progmob.tracking.presentation


sealed class TrackingEvent {
    data object StartTrackingService: TrackingEvent()
    data object PauseTrackingService: TrackingEvent()
    data object StopTrackingService: TrackingEvent()
}
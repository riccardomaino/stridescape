package it.unito.progmob.tracking.domain.usecase

data class TrackingUseCases(
    val startTrackingUseCase: StartTrackingUseCase,
    val resumeTrackingUseCase: ResumeTrackingUseCase,
    val pauseTrackingUseCase: PauseTrackingUseCase,
    val stopTrackingUseCase: StopTrackingUseCase
)
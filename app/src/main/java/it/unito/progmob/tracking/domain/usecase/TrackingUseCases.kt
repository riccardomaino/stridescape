package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase

data class TrackingUseCases(
    val startTrackingUseCase: StartTrackingUseCase,
    val resumeTrackingUseCase: ResumeTrackingUseCase,
    val pauseTrackingUseCase: PauseTrackingUseCase,
    val stopTrackingUseCase: StopTrackingUseCase,
    val readUserWeightEntryUseCase: ReadUserWeightEntryUseCase,
    val readUserHeightEntryUseCase: ReadUserHeightEntryUseCase
)
package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.AddPathPointUseCase
import it.unito.progmob.core.domain.usecase.AddWalkUseCase

data class TrackingUseCases(
    val startTrackingUseCase: StartTrackingUseCase,
    val resumeTrackingUseCase: ResumeTrackingUseCase,
    val pauseTrackingUseCase: PauseTrackingUseCase,
    val stopTrackingUseCase: StopTrackingUseCase,
    val readUserWeightEntryUseCase: ReadUserWeightEntryUseCase,
    val readUserHeightEntryUseCase: ReadUserHeightEntryUseCase,
    val addPathPointUseCase: AddPathPointUseCase,
    val addWalkUseCase: AddWalkUseCase
)
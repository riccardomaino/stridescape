package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.presentation.state.UiTrackingState

class UpsertWalkUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(uiTrackingState: UiTrackingState): Long {
        val newWalkEntity = WalkEntity(
            weekDay = DateUtils.getCurrentDayOfWeek(),
            date = DateUtils.getCurrentDate(pattern = "dd/MM/yyyy"),
            steps = uiTrackingState.steps,
            distance = uiTrackingState.distanceInMeters,
            time = uiTrackingState.timeInMillis,
            calories = uiTrackingState.caloriesBurnt,
            averageSpeed = WalkUtils.getAverageSpeedInKmH(uiTrackingState.pathPoints)
        )
        return walkRepository.upsertNewWalk(newWalkEntity)
    }
}
package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.presentation.state.UiTrackingState

class AddWalkUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(uiTrackingState: UiTrackingState): Long {
        val newWalkEntity = WalkEntity(
            weekDay = DateUtils.getCurrentDayOfWeek(),
            date = DateUtils.formattedCurrentDate(),
            month = DateUtils.getCurrentMonth(),
            steps = uiTrackingState.steps,
            distance = uiTrackingState.distanceInMeters,
            time = uiTrackingState.timeInMillis,
            calories = uiTrackingState.caloriesBurnt,
            averageSpeed = WalkUtils.getAverageSpeedInKmH(uiTrackingState.pathPoints)
        )
        return walkRepository.upsertNewWalk(newWalkEntity)
    }
}
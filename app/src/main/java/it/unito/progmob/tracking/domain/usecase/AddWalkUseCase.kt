package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.presentation.state.UiTrackingState

/**
 * Use case for adding a new walk to the database based on the provided UI tracking state.
 *
 * @param walkRepository The repository responsible for storing walk data.
 */
class AddWalkUseCase(
    private val walkRepository: WalkRepository
) {
    /**
     * Adds a new walk to the database based on the provided [UiTrackingState].
     *
     * @param uiTrackingState The current state of the UI tracking.
     * @return The id of the newly added walk.
     */
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
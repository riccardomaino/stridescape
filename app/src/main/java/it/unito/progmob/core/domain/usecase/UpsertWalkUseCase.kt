package it.unito.progmob.core.domain.usecase

import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import java.time.Instant

class UpsertWalkUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(uiTrackingState: UiTrackingState): Long {
        val newWalkEntity = WalkEntity(
            weekDay = DateUtils.getCurrentDayOfWeek(),
            date = getCurrentDate(),
            steps = uiTrackingState.steps,
            distance = uiTrackingState.distanceInMeters,
            time = uiTrackingState.timeInMillis,
            calories = uiTrackingState.caloriesBurnt,
            averageSpeed = getAverageSpeed(uiTrackingState.pathPoints)
        )
        return walkRepository.upsertNewWalk(newWalkEntity)
    }

    private fun getCurrentDate(): String {
        val currentTimeStamp = Instant.now().epochSecond
        return TimeUtils.formatEpochTime(currentTimeStamp, "dd/MM/yyyy")
    }

    private fun getAverageSpeed(pathPoints: List<PathPoint>): Float {
        var speed = 0f
        var numLocationPoints = 0
        pathPoints.forEach { point ->
            if(point is PathPoint.LocationPoint){
                speed += point.speed
                numLocationPoints++
            }
        }
        return if(numLocationPoints > 0) speed / numLocationPoints else 0f
    }
}
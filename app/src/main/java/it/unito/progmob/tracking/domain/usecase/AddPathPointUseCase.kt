package it.unito.progmob.tracking.domain.usecase

import android.util.Log
import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.repository.WalkRepository

class AddPathPointUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(walkId: Long, newPathPoint: PathPoint) {
        val pathPointEntity = PathPointEntity(
            walkId = walkId,
            pathPoint = newPathPoint
        )
        Log.d("AddPathPointUseCase", "Adding path point: ${pathPointEntity.pathPoint}")
        walkRepository.upsertNewPathPoint(pathPointEntity)
    }
}
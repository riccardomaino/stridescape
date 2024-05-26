package it.unito.progmob.core.domain.usecase

import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.repository.WalkRepository

class UpsertNewPathPointUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(newPathPoint: PathPoint, walkId: Long) {
        val pathPointEntity = PathPointEntity(
            walkId = walkId,
            pathPoint = newPathPoint
        )
        walkRepository.upsertNewPathPoint(pathPointEntity)
    }
}
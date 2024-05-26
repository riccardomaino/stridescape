package it.unito.progmob.core.domain.usecase

import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.repository.WalkRepository

class UpsertPathPointUseCase(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(walkId: Long, newPathPoint: PathPoint) {
        val pathPointEntity = PathPointEntity(
            walkId = walkId,
            pathPoint = newPathPoint
        )
        walkRepository.upsertNewPathPoint(pathPointEntity)
    }
}
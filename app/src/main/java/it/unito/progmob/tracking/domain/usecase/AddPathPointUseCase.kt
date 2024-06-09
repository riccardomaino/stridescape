package it.unito.progmob.tracking.domain.usecase

import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.tracking.domain.model.PathPoint

/**
 * Use case for adding a new [PathPoint] to a walk in the database.
 *
 * @param walkRepository The repository responsible for storing walk path points.
 */
class AddPathPointUseCase(
    private val walkRepository: WalkRepository
) {
    /**
     * Adds a new [PathPoint] to the walk with the given [walkId].
     *
     * @param walkId The id of the walk to add the path point to.
     * @param newPathPoint The [PathPoint] to add.
     */
    suspend operator fun invoke(walkId: Long, newPathPoint: PathPoint) {
        val pathPointEntity = PathPointEntity(
            walkId = walkId,
            pathPoint = newPathPoint
        )
        walkRepository.upsertNewPathPoint(pathPointEntity)
    }
}
package it.unito.progmob.core.data.repository

import it.unito.progmob.core.data.local.TargetDao
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing step target data, providing access to the data access object.
 *
 * @property targetDao The Data AccessObject for interacting with step target data.
 */
class TargetRepositoryImpl(
    private val targetDao: TargetDao
): TargetRepository {

    /**
     * Retrieves the step target for a given date.
     *
     * @param date The date for which to retrieve the target.* @return A flow emitting the step target for the given date.
     */
    override fun findTargetByDate(date: String): Flow<Int> {
        return targetDao.findTargetByDate(date)
    }

    /**
     * Retrieves the most recently set step target.
     *
     * @return A flow emitting the most recently set step target.
     */
    override fun findLastTarget(): Flow<Int> {
        return targetDao.findLastTarget()
    }

    /**
     * Inserts or updates a new step target in the database.
     *
     * @param newTargetEntity The step target entity to insert or update.
     */
    override suspend fun upsertNewTarget(newTargetEntity: TargetEntity) {
        targetDao.upsertNewTarget(newTargetEntity)
    }
}
package it.unito.progmob.core.data.repository

import it.unito.progmob.core.data.local.TargetDao
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.Flow

class TargetRepositoryImpl(
    private val targetDao: TargetDao
): TargetRepository {

    override fun findTargetByDate(date: String): Flow<Int> {
        return targetDao.findTargetByDate(date)
    }

    override fun findLastTarget(): Flow<Int> {
        return targetDao.findLastTarget()
    }

    override suspend fun upsertNewTarget(newTargetEntity: TargetEntity) {
        targetDao.upsertNewTarget(newTargetEntity)
    }
}
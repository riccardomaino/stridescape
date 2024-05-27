package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.TargetEntity
import kotlinx.coroutines.flow.Flow

interface TargetRepository {
    fun findTargetByDate(date: String): Flow<Int>
    fun findLastTarget(): Flow<Int>
    suspend fun upsertNewTarget(newTargetEntity: TargetEntity)
}
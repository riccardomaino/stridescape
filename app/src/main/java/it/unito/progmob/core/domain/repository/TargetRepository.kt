package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.model.tuples.DateTargetTuple
import kotlinx.coroutines.flow.Flow

interface TargetRepository {
    fun findTargetByDate(date: String): Flow<Int>
    fun findLastTarget(): Flow<Int>
    fun findTargetBetweenDates(startDate: String, endDate: String): Flow<List<DateTargetTuple>?>
    suspend fun upsertNewTarget(newTargetEntity: TargetEntity)
}
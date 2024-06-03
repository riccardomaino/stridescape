package it.unito.progmob.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import it.unito.progmob.core.domain.model.TargetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TargetDao {

    @Query("SELECT stepsTarget FROM targets WHERE date = :date")
    fun findTargetByDate(date: String): Flow<Int>

    @Query("SELECT stepsTarget FROM targets ORDER BY date DESC LIMIT 1")
    fun findLastTarget(): Flow<Int>

    @Upsert
    suspend fun upsertNewTarget(newTargetEntity: TargetEntity)
}
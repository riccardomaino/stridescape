package it.unito.progmob.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import it.unito.progmob.core.domain.model.TargetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for interacting with step target data in the database.
 */
@Dao
interface TargetDao {

    /**
     * Retrieves the step target for a given date.
     *
     * @param date The date for which to retrieve the target.
     * @return A flow emitting the step target for the given date.
     */
    @Query("SELECT stepsTarget FROM targets WHERE date = :date")
    fun findTargetByDate(date: String): Flow<Int>

    /**
     * Retrieves the most recently set step target.
     *
     * @return A flow emitting the most recently set step target.
     */
    @Query("SELECT stepsTarget FROM targets ORDER BY date DESC LIMIT 1")
    fun findLastTarget(): Flow<Int>

    /**
     * Inserts or updates a new step target in the database for the current day.
     *
     * @param newTargetEntity The step target entity to insert or update.
     */
    @Upsert
    suspend fun upsertNewTarget(newTargetEntity: TargetEntity)
}
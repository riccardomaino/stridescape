package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import kotlinx.coroutines.flow.Flow

interface WalkRepository {
    fun findWalksWithPathPoints(): Flow<List<WalkWithPathPoints>>
    fun findStepsByDate(date: String): Flow<Int>
    fun findCaloriesByDate(date: String): Flow<Int>
    fun findDistanceByDate(date: String): Flow<Int>
    fun findTimeByDate(date: String): Flow<Long>

    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)
    suspend fun deleteWalk(walkEntity: WalkEntity)
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)

}
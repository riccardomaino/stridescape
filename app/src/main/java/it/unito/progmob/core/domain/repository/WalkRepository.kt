package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import kotlinx.coroutines.flow.Flow

interface WalkRepository {
    fun getWalksWithPathPoints(): Flow<List<WalkWithPathPoints>>
    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)
    suspend fun deleteWalk(walkEntity: WalkEntity)
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)
}
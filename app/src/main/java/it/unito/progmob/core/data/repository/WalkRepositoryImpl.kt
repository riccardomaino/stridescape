package it.unito.progmob.core.data.repository

import it.unito.progmob.core.data.data_source.WalkDao
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow

class WalkRepositoryImpl(
    private val walkDao: WalkDao
): WalkRepository {
    override fun findWalksWithPathPoints(): Flow<List<WalkWithPathPoints>> {
        return walkDao.findWalksWithPathPoints()
    }

    override fun findStepsByDate(currentDay: String): Flow<Int> {
        return walkDao.findStepsByDate(currentDay)
    }

    override fun findCaloriesByDate(currentDay: String): Flow<Int> {
        return walkDao.findCaloriesByDate(currentDay)
    }

    override fun findDistanceByDate(currentDay: String): Flow<Int> {
        return walkDao.findDistanceByDate(currentDay)
    }

    override fun findTimeByDate(currentDay: String): Flow<Long> {
        return walkDao.findTimeByDate(currentDay)
    }

    override suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long {
        return walkDao.upsertNewWalk(newWalkEntity)
    }

    override suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity) {
        walkDao.upsertNewPathPoint(newPathPointEntity)
    }

    override suspend fun deleteWalk(walkEntity: WalkEntity) {
        walkDao.deleteWalk(walkEntity)
    }

    override suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity) {
        walkDao.deleteSinglePathPoint(pathPointEntity)
    }

    override suspend fun deleteAllPathPointsOfWalk(walkId: Int) {
        walkDao.deleteAllPathPointsOfWalk(walkId)
    }

}
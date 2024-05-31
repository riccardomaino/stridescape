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

    override fun findStepsByDate(date: String): Flow<Int> {
        return walkDao.findStepsByDate(date)
    }

    override fun findCaloriesByDate(date: String): Flow<Int> {
        return walkDao.findCaloriesByDate(date)
    }

    override fun findDistanceByDate(date: String): Flow<Int> {
        return walkDao.findDistanceByDate(date)
    }

    override fun findTimeByDate(date: String): Flow<Long> {
        return walkDao.findTimeByDate(date)
    }

    override fun findStepsBetweenDates(startDate: String, endDate: String): Flow<IntArray> {
        return walkDao.findStepsBetweenDates(startDate, endDate)
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
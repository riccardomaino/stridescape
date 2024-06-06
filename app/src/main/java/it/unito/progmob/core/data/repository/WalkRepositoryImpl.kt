package it.unito.progmob.core.data.repository

import it.unito.progmob.core.data.local.WalkDao
import it.unito.progmob.core.domain.model.tuples.DateCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.DateDistanceTuple
import it.unito.progmob.core.domain.model.tuples.DateSpeedTuple
import it.unito.progmob.core.domain.model.tuples.DateStepsTuple
import it.unito.progmob.core.domain.model.tuples.DateTimeTuple
import it.unito.progmob.core.domain.model.tuples.WeekDayStepsTuple
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPointsEntity
import it.unito.progmob.core.domain.model.tuples.DateTargetTuple
import it.unito.progmob.core.domain.model.tuples.MonthCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.MonthDistanceTuple
import it.unito.progmob.core.domain.model.tuples.MonthSpeedTuple
import it.unito.progmob.core.domain.model.tuples.MonthStepsTuple
import it.unito.progmob.core.domain.model.tuples.MonthTimeTuple
import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow

class WalkRepositoryImpl(
    private val walkDao: WalkDao
): WalkRepository {
    override fun findWalksWithPathPoints(): List<WalkWithPathPointsEntity> {
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

    override fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?> {
        return walkDao.findStepsBetweenDates(startDate, endDate)
    }

    override fun findTargetBetweenDates(startDate: String, endDate: String): Flow<List<DateTargetTuple>?> {
        return walkDao.findTargetBetweenDates(startDate, endDate)
    }

    override fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>? {
        return walkDao.findDistanceForDateRange(startDate, endDate)
    }

    override fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>? {
        return walkDao.findTimeForDateRange(startDate, endDate)
    }

    override fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>? {
        return walkDao.findCaloriesForDateRange(startDate, endDate)
    }

    override fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>? {
        return walkDao.findStepsForDateRange(startDate, endDate)
    }

    override fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>? {
        return walkDao.findSpeedForDateRange(startDate, endDate)
    }

    override fun findDistanceForYear(year: String): List<MonthDistanceTuple>? {
        return walkDao.findDistanceForYear(year)
    }

    override fun findTimeForYear(year: String): List<MonthTimeTuple>? {
        return walkDao.findTimeForYear(year)
    }

    override fun findCaloriesForYear(year: String): List<MonthCaloriesTuple>? {
        return walkDao.findCaloriesForYear(year)
    }

    override fun findStepsForYear(year: String): List<MonthStepsTuple>? {
        return walkDao.findStepsForYear(year)
    }

    override fun findSpeedForYear(year: String): List<MonthSpeedTuple>? {
        return walkDao.findSpeedForYear(year)
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
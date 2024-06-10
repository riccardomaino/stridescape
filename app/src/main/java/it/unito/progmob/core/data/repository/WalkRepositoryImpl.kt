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

/**
 * Repository for managing walk and path point data, providing access to the data access object.
 *
 * @property walkDao TheData Access Object for interacting with walk and path point data.
 */
class WalkRepositoryImpl(
    private val walkDao: WalkDao
): WalkRepository {
    /**
     * Retrieves a list of walks with their associated path points within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of WalkWithPathPointsEntity objects representing the walks and their path points.
     */
    override fun findWalksWithPathPoints(startDate: String, endDate: String): List<WalkWithPathPointsEntity>? {
        return walkDao.findWalksWithPathPoints(startDate, endDate)
    }

    /**
     * Retrieves the total number of steps taken for a given date.
     *
     * @param date The date for which to retrieve the steps.
     * @return A flow emitting the total number of steps taken for the given date.
     */
    override fun findStepsByDate(date: String): Flow<Int> {
        return walkDao.findStepsByDate(date)
    }

    /**
     * Retrieves the total calories burned for a given date.
     *
     * @param date The date for which to retrieve the calories.
     * @return A flow emitting the total calories burned for the given date.
     */
    override fun findCaloriesByDate(date: String): Flow<Int> {
        return walkDao.findCaloriesByDate(date)
    }

    /**
     * Retrieves the total distance walked for a given date.
     *
     * @param date The date for which to retrieve the distance.
     * @return A flow emitting the total distance walked for the given date.
     */
    override fun findDistanceByDate(date: String): Flow<Int> {
        return walkDao.findDistanceByDate(date)
    }

    /**
     * Retrieves the total time spent walking for a given date.
     *
     * @param date The date for which to retrieve the time.
     * @return A flow emitting the total time spent walking for the given date, in milliseconds.
     */
    override fun findTimeByDate(date: String): Flow<Long> {
        return walkDao.findTimeByDate(date)
    }

    /**
     * Retrieves the total number of steps taken for each day of the week within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of WeekDayStepsTuple objects representing the total steps for each day of the week.
     */
    override fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?> {
        return walkDao.findStepsBetweenDates(startDate, endDate)
    }

    /**
     * Retrieves the daily step targets for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of DateTargetTuple objects representing the daily step targets.
     */
    override fun findTargetBetweenDates(startDate: String, endDate: String): Flow<List<DateTargetTuple>?> {
        return walkDao.findTargetBetweenDates(startDate, endDate)
    }

    /**
     * Retrieves the total distance walked for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateDistanceTuple objects representing the total distance walked for each day.
     */
    override fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>? {
        return walkDao.findDistanceForDateRange(startDate, endDate)
    }

    /**
     * Retrieves the total time spent walking for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateTimeTuple objects representing the total time spent walking for each day.
     */
    override fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>? {
        return walkDao.findTimeForDateRange(startDate, endDate)
    }

    /**
     * Retrieves the total calories burned for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateCaloriesTuple objects representing the total calories burned for each day.
     */
    override fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>? {
        return walkDao.findCaloriesForDateRange(startDate, endDate)
    }

    /**
     * Retrieves the total number of steps taken for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateStepsTuple objects representing the total number of steps taken for each day.
     */
    override fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>? {
        return walkDao.findStepsForDateRange(startDate, endDate)
    }

    /**
     * Retrieves the average speed for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateSpeedTuple objects representing the average speed for each day.
     */
    override fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>? {
        return walkDao.findSpeedForDateRange(startDate, endDate)
    }

    /**
     * Retrieves the total distance walked for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthDistanceTuple objects representing the total distance walked for each month.
     */
    override fun findDistanceForYear(year: String): List<MonthDistanceTuple>? {
        return walkDao.findDistanceForYear(year)
    }

    /**
     * Retrieves the total time spent walking for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthTimeTuple objects representing the total time spent walking for each month.
     */
    override fun findTimeForYear(year: String): List<MonthTimeTuple>? {
        return walkDao.findTimeForYear(year)
    }

    /**
     * Retrieves the total calories burned for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthCaloriesTuple objects representing the total calories burned for each month.
     */
    override fun findCaloriesForYear(year: String): List<MonthCaloriesTuple>? {
        return walkDao.findCaloriesForYear(year)
    }

    /**
     * Retrieves the total number of steps taken for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthStepsTuple objects representing the total number of steps taken for each month.
     */
    override fun findStepsForYear(year: String): List<MonthStepsTuple>? {
        return walkDao.findStepsForYear(year)
    }

    /**
     * Retrieves the average speed for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthSpeedTuple objects representing the average speed for each month.
     */
    override fun findSpeedForYear(year: String): List<MonthSpeedTuple>? {
        return walkDao.findSpeedForYear(year)
    }

    /**
     * Inserts or updates a new walk in the database.
     *
     * @param newWalkEntity The walk entity to insert or update.
     * @return The ID of the inserted or updated walk.
     */
    override suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long {
        return walkDao.upsertNewWalk(newWalkEntity)
    }

    /**
     * Inserts or updates a new path point in the database.
     *
     * @param newPathPointEntity The path point entity to insert or update.
     */
    override suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity) {
        walkDao.upsertNewPathPoint(newPathPointEntity)
    }

    /**
     * Deletes a walk from the database.
     *
     * @param walkEntity The walk entity to delete.
     */
    override suspend fun deleteWalk(walkEntity: WalkEntity) {
        walkDao.deleteWalk(walkEntity)
    }

    /**
     * Deletes a single path point from the database.
     *
     * @param pathPointEntity The path point entity to delete.
     */
    override suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity) {
        walkDao.deleteSinglePathPoint(pathPointEntity)
    }

    /**
     * Deletes all path points associated with a given walk.
     *
     * @param walkId The ID of the walk for which to delete the path points.
     */
    override suspend fun deleteAllPathPointsOfWalk(walkId: Int) {
        walkDao.deleteAllPathPointsOfWalk(walkId)
    }
}
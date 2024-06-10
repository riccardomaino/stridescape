package it.unito.progmob.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unito.progmob.core.domain.model.tuples.DateCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.DateDistanceTuple
import it.unito.progmob.core.domain.model.tuples.DateSpeedTuple
import it.unito.progmob.core.domain.model.tuples.DateStepsTuple
import it.unito.progmob.core.domain.model.tuples.DateTimeTuple
import it.unito.progmob.core.domain.model.tuples.MonthDistanceTuple
import it.unito.progmob.core.domain.model.tuples.WeekDayStepsTuple
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPointsEntity
import it.unito.progmob.core.domain.model.tuples.DateTargetTuple
import it.unito.progmob.core.domain.model.tuples.MonthCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.MonthSpeedTuple
import it.unito.progmob.core.domain.model.tuples.MonthStepsTuple
import it.unito.progmob.core.domain.model.tuples.MonthTimeTuple
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for interacting with walk and path point data in the database.
 */
@Dao
interface WalkDao {
    /**
     * Retrieves a list of walks with their associated path points within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of WalkWithPathPointsEntity objects representing the walks and their path points.
     */
    @Transaction
    @Query("SELECT * FROM walks WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun findWalksWithPathPoints(startDate: String, endDate: String): List<WalkWithPathPointsEntity>?

    /**
     * Retrieves the total number of steps taken for a given date.
     *
     * @param date The date for which to retrieve the steps.
     * @return A flow emitting the total number of steps taken for the given date.
     */
    @Query("SELECT SUM(steps) FROM walks WHERE date = :date")
    fun findStepsByDate(date: String): Flow<Int>

    /**
     * Retrieves the total calories burned for a given date.
     *
     * @param date The date for which to retrieve the calories.
     * @return A flow emitting the total calories burned for the given date.
     */
    @Query("SELECT SUM(calories) FROM walks WHERE date = :date")
    fun findCaloriesByDate(date: String): Flow<Int>

    /**
     * Retrieves the total distance walked for a given date.
     *
     * @param date The date for which to retrieve the distance.
     * @return A flow emitting the total distance walked for the given date.
     */
    @Query("SELECT SUM(distance) FROM walks WHERE date = :date")
    fun findDistanceByDate(date: String): Flow<Int>

    /**
     * Retrieves the total time spent walking for a given date.
     *
     * @param date The date for which to retrieve the time.
     * @return A flow emitting the total time spent walking for the given date, inmilliseconds.
     */
    @Query("SELECT SUM(time) FROM walks WHERE date = :date")
    fun findTimeByDate(date: String): Flow<Long>

    /**
     * Retrieves the total number of steps taken for each day of the week within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of WeekDayStepsTuple objects representing the total steps for each day of the week.
     */
    @Query("SELECT weekDay, SUM(steps) AS steps FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY weekDay")
    fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?>

    /**
     * Retrieves the daily step targets for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of DateTargetTuple objects representing the daily step targets.
     */
    @Query("SELECT date, stepsTarget FROM targets WHERE date >= :startDate AND date <= :endDate")
    fun findTargetBetweenDates(startDate: String, endDate: String): Flow<List<DateTargetTuple>?>

    /**
     * Retrieves the total distance walked for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateDistanceTuple objects representing the total distance walked for each day.
     */
    @Query("SELECT date, SUM(distance) AS distance FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>?

    /**
     * Retrieves the total time spent walking for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateTimeTuple objects representing the total time spent walking for each day.
     */
    @Query("SELECT date, SUM(time) AS time FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>?

    /**
     * Retrieves the total calories burned for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateCaloriesTuple objects representing the total calories burned for each day.
     */
    @Query("SELECT date, SUM(calories) AS calories FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>?

    /**
     * Retrieves the total number of steps taken for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateStepsTuple objects representing the total number of steps taken for each day.
     */
    @Query("SELECT date, SUM(steps) AS steps FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>?

    /**
     * Retrieves the average speed for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateSpeedTuple objects representing the average speed for each day.
     */
    @Query("SELECT date, AVG(averageSpeed) AS averageSpeed FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>?


    /**
     * Retrieves the total distance walked for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthDistanceTuple objects representing the total distance walked for each month.
     */
    @Query("SELECT month, SUM(distance) AS distance FROM walks WHERE date LIKE :year || '%' GROUP BY month ORDER BY month ASC")
    fun findDistanceForYear(year: String): List<MonthDistanceTuple>?

    /**
     * Retrieves the total time spent walking for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthTimeTuple objects representing the total time spent walking for each month.
     */
    @Query("SELECT month, SUM(time) AS time FROM walks WHERE date LIKE :year || '%' GROUP BY month ORDER BY month ASC")
    fun findTimeForYear(year: String): List<MonthTimeTuple>?

    /**
     * Retrieves the total calories burned for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthCaloriesTuple objects representing the total calories burned for each month.
     */
    @Query("SELECT month, SUM(calories) AS calories FROM walks WHERE date LIKE :year || '%' GROUP BY month ORDER BY month ASC")
    fun findCaloriesForYear(year: String): List<MonthCaloriesTuple>?

    /**
     * Retrieves the total number of steps taken for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthStepsTuple objects representing the total number of steps taken for each month.
     */
    @Query("SELECT month, SUM(steps) AS steps FROM walks WHERE date LIKE :year || '%' GROUP BY month ORDER BY month ASC")
    fun findStepsForYear(year: String): List<MonthStepsTuple>?

    /**
     * Retrieves the average speed for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthSpeedTuple objects representing the average speed for each month.
     */
    @Query("SELECT month, AVG(averageSpeed) AS averageSpeed FROM walks WHERE date LIKE :year || '%' GROUP BY month ORDER BY month ASC")
    fun findSpeedForYear(year: String): List<MonthSpeedTuple>?

    /**
     * Inserts or updates a new walk in the database.
     *
     * @param newWalkEntity The walk entity to insert or update.
     * @return The ID of the inserted or updated walk.
     */
    @Upsert
    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long

    /**
     * Inserts or updates a new path point in the database.
     *
     * @param newPathPointEntity The path point entity to insert or update.
     */
    @Upsert
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)

    /**
     * Deletes a walk from the database.
     *
     * @param walkEntity The walk entity to delete.
     */
    @Delete
    suspend fun deleteWalk(walkEntity: WalkEntity)

    /**
     * Deletes a single path point from the database.
     *
     * @param pathPointEntity The path point entity to delete.
     */
    @Delete
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)

    /**
     * Deletes all path points associated with a given walk.
     *
     * @param walkId The ID of the walk for which to delete the path points.
     */
    @Query("DELETE FROM path_points WHERE walkId = :walkId")
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)
}
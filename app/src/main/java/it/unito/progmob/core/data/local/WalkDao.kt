package it.unito.progmob.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unito.progmob.core.domain.model.DateCaloriesTuple
import it.unito.progmob.core.domain.model.DateDistanceTuple
import it.unito.progmob.core.domain.model.DateSpeedTuple
import it.unito.progmob.core.domain.model.DateStepsTuple
import it.unito.progmob.core.domain.model.DateTimeTuple
import it.unito.progmob.core.domain.model.WeekDayStepsTuple
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import kotlinx.coroutines.flow.Flow

@Dao
interface WalkDao {

    @Transaction
    @Query("SELECT * FROM walks")
    fun findWalksWithPathPoints(): Flow<List<WalkWithPathPoints>>

    @Query("SELECT SUM(steps) FROM walks WHERE date = :date")
    fun findStepsByDate(date: String): Flow<Int>

    @Query("SELECT SUM(calories) FROM walks WHERE date = :date")
    fun findCaloriesByDate(date: String): Flow<Int>

    @Query("SELECT SUM(distance) FROM walks WHERE date = :date")
    fun findDistanceByDate(date: String): Flow<Int>

    @Query("SELECT SUM(time) FROM walks WHERE date = :date")
    fun findTimeByDate(date: String): Flow<Long>

    @Query("SELECT weekDay, SUM(steps) AS steps FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date, weekDay")
    fun findStepsBetweenDates(startDate: String, endDate: String): Flow<WeekDayStepsTuple?>

    @Query("SELECT date, SUM(distance) AS distance FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>?

    @Query("SELECT date, SUM(time) AS time FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>?

    @Query("SELECT date, SUM(calories) AS calories FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>?

    @Query("SELECT date, SUM(steps) AS steps FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>?

    @Query("SELECT date, AVG(averageSpeed) AS averageSpeed FROM walks WHERE date >= :startDate AND date <= :endDate GROUP BY date ORDER BY date ASC")
    fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>?

    @Upsert
    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    @Upsert
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)
    @Delete
    suspend fun deleteWalk(walkEntity: WalkEntity)
    @Delete
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)
    @Query("DELETE FROM path_points WHERE walkId = :walkId")
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)
}
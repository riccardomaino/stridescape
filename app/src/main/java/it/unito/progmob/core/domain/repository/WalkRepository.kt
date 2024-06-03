package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.DateCaloriesTuple
import it.unito.progmob.core.domain.model.DateDistanceTuple
import it.unito.progmob.core.domain.model.DateSpeedTuple
import it.unito.progmob.core.domain.model.DateStepsTuple
import it.unito.progmob.core.domain.model.WeekDayStepsTuple
import it.unito.progmob.core.domain.model.DateTimeTuple
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
    fun findStepsBetweenDates(startDate: String, endDate: String): Flow<WeekDayStepsTuple?>
    fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>?
    fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>?
    fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>?
    fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>?
    fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>?



    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)
    suspend fun deleteWalk(walkEntity: WalkEntity)
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)

}
package it.unito.progmob.core.domain.repository

import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPointsEntity
import it.unito.progmob.core.domain.model.tuples.DateCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.DateDistanceTuple
import it.unito.progmob.core.domain.model.tuples.DateSpeedTuple
import it.unito.progmob.core.domain.model.tuples.DateStepsTuple
import it.unito.progmob.core.domain.model.tuples.DateTimeTuple
import it.unito.progmob.core.domain.model.tuples.MonthCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.MonthDistanceTuple
import it.unito.progmob.core.domain.model.tuples.MonthSpeedTuple
import it.unito.progmob.core.domain.model.tuples.MonthStepsTuple
import it.unito.progmob.core.domain.model.tuples.MonthTimeTuple
import it.unito.progmob.core.domain.model.tuples.WeekDayStepsTuple
import kotlinx.coroutines.flow.Flow

interface WalkRepository {
    fun findWalksWithPathPoints(startDate: String, endDate: String): List<WalkWithPathPointsEntity>?
    fun findWalkById(id: Long): WalkEntity?
    fun findPathPointById(id: Long): PathPointEntity?
    fun findStepsByDate(date: String): Flow<Int>
    fun findCaloriesByDate(date: String): Flow<Int>
    fun findDistanceByDate(date: String): Flow<Int>
    fun findTimeByDate(date: String): Flow<Long>
    fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?>
    fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>?
    fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>?
    fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>?
    fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>?
    fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>?
    fun findDistanceForYear(year: String): List<MonthDistanceTuple>?
    fun findTimeForYear(year: String): List<MonthTimeTuple>?
    fun findCaloriesForYear(year: String): List<MonthCaloriesTuple>?
    fun findStepsForYear(year: String): List<MonthStepsTuple>?
    fun findSpeedForYear(year: String): List<MonthSpeedTuple>?

    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)
}
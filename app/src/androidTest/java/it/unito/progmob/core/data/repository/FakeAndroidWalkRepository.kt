package it.unito.progmob.core.data.repository

import it.unito.progmob.core.domain.model.tuples.DateCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.DateDistanceTuple
import it.unito.progmob.core.domain.model.tuples.DateSpeedTuple
import it.unito.progmob.core.domain.model.tuples.DateStepsTuple
import it.unito.progmob.core.domain.model.tuples.DateTimeTuple
import it.unito.progmob.core.domain.model.tuples.WeekDayStepsTuple
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPointsEntity
import it.unito.progmob.core.domain.model.tuples.MonthCaloriesTuple
import it.unito.progmob.core.domain.model.tuples.MonthDistanceTuple
import it.unito.progmob.core.domain.model.tuples.MonthSpeedTuple
import it.unito.progmob.core.domain.model.tuples.MonthStepsTuple
import it.unito.progmob.core.domain.model.tuples.MonthTimeTuple
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAndroidWalkRepository: WalkRepository {

    private var walkItems = mutableListOf<WalkEntity>()
    private var pathPointItems = mutableListOf<PathPointEntity>()

    fun shouldHaveFilledWalkList(shouldHaveFilledList: Boolean) {
        walkItems = if (shouldHaveFilledList) {
            mutableListOf(
                WalkEntity(1, 0, "2024-01-01", 1000, 1000, 1000, 1000, 1000, 1.2f),
                WalkEntity(2, 1, "2024-01-02", 2000, 2000, 2000, 2000, 2000, 1.4f),
                WalkEntity(3, 2, "2024-01-03", 3000, 3000, 3000, 3000, 3000, 2.3f),
                WalkEntity(4, 3, "2024-02-04", 4000, 4000, 4000, 4000, 4000, 2f),
                WalkEntity(5, 4, "2024-02-05", 5000, 5000, 5000, 5000, 5000, 1.5f),
                WalkEntity(6, 5, "2024-03-06", 6000, 6000, 6000, 6000, 6000, 1.7f),
                WalkEntity(7, 6, "2024-03-07", 7000, 7000, 7000, 7000, 7000, 1.9f)
            )
        } else {
            mutableListOf()
        }
    }

    fun shouldHaveFilledPathPointList(shouldHaveFilledList: Boolean) {
        pathPointItems = if (shouldHaveFilledList) {
            mutableListOf(
                PathPointEntity(1, 1, PathPoint.LocationPoint(1.0, 1.0, 2f)),
                PathPointEntity(2, 1, PathPoint.LocationPoint(2.0, 2.0, 2f)),
                PathPointEntity(3, 3, PathPoint.LocationPoint(3.0, 3.0, 1.3f)),
                PathPointEntity(4, 3, PathPoint.EmptyPoint),
                PathPointEntity(5, 5, PathPoint.LocationPoint(5.0, 5.0, 1.4f)),
                PathPointEntity(6, 6, PathPoint.LocationPoint(6.0, 6.0, 3f)),
                PathPointEntity(7, 7, PathPoint.LocationPoint(7.0, 7.0, 1f))
            )
        } else {
            mutableListOf()
        }
    }

    /**
     * Retrieves a list of walks with their associated path points within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of WalkWithPathPointsEntity objects representing the walks and their path points.
     */
    override fun findWalksWithPathPoints(startDate: String, endDate: String): List<WalkWithPathPointsEntity> {
        return walkItems.map {
            WalkWithPathPointsEntity(it, mutableListOf())
        }
    }

    override fun findWalkById(id: Long): WalkEntity {
        return walkItems.first {
            it.id?.toLong() == id
        }
    }

    override fun findPathPointById(id: Long): PathPointEntity? {
        return pathPointItems.firstOrNull {
            it.id?.toLong() == id
        }
    }

    /**
     * Retrieves the total number of steps taken for a given date.
     *
     * @param date The date for which to retrieve the steps.
     * @return A flow emitting the total number of steps taken for the given date.
     */
    override fun findStepsByDate(date: String): Flow<Int> {
        return flowOf(
            walkItems.first {
                it.date == date
            }.steps
        )
    }

    /**
     * Retrieves the total calories burned for a given date.
     *
     * @param date The date for which to retrieve the calories.
     * @return A flow emitting the total calories burned for the given date.
     */
    override fun findCaloriesByDate(date: String): Flow<Int> {
        return flowOf(
            walkItems.first {
                it.date == date
            }.calories
        )
    }

    /**
     * Retrieves the total distance walked for a given date.
     *
     * @param date The date for which to retrieve the distance.
     * @return A flow emitting the total distance walked for the given date.
     */
    override fun findDistanceByDate(date: String): Flow<Int> {
        return flowOf(
            walkItems.first {
                it.date == date
            }.distance
        )
    }

    /**
     * Retrieves the total time spent walking for a given date.
     *
     * @param date The date for which to retrieve the time.
     * @return A flow emitting the total time spent walking for the given date, in milliseconds.
     */
    override fun findTimeByDate(date: String): Flow<Long> {
        return flowOf(
            walkItems.first {
                it.date == date
            }.time
        )
    }

    /**
     * Retrieves the total number of steps taken for each day of the week within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of WeekDayStepsTuple objects representing the total steps for each day of the week.
     */
    override fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?> {
        return flowOf(
            walkItems.filter {
                it.date in startDate..endDate
            }.map {
                WeekDayStepsTuple(it.weekDay, it.steps)
            }
        )
    }

    /**
     * Retrieves the total distance walked for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateDistanceTuple objects representing the total distance walked for each day.
     */
    override fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple> {
        return walkItems.map {
            DateDistanceTuple(it.date, it.distance)
        }
    }

    /**
     * Retrieves the total time spent walking for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateTimeTuple objects representing the total time spent walking for each day.
     */
    override fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple> {
        return walkItems.filter {
                it.date in startDate..endDate
            }.map {
                DateTimeTuple(it.date, it.time)
            }
    }

    /**
     * Retrieves the total calories burned for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateCaloriesTuple objects representing the total calories burned for each day.
     */
    override fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple> {
        return walkItems.filter {
                it.date in startDate..endDate
            }.map {
                DateCaloriesTuple(it.date, it.calories)
            }
    }

    /**
     * Retrieves the total number of steps taken for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateStepsTuple objects representing the total number of steps taken for each day.
     */
    override fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple> {
        return walkItems.filter {
                it.date in startDate..endDate
            }.map {
                DateStepsTuple(it.date, it.steps)
            }
    }

    /**
     * Retrieves the average speed for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of DateSpeedTuple objects representing the average speed for each day.
     */
    override fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple> {
        return walkItems.filter {
                it.date in startDate..endDate
            }.map {
                DateSpeedTuple(it.date, it.averageSpeed)
            }
    }

    /**
     * Retrieves the total distance walked for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthDistanceTuple objects representing the total distance walked for each month.
     */
    override fun findDistanceForYear(year: String): List<MonthDistanceTuple> {
        return walkItems.filter {
            it.date.startsWith(year)
        }.map {
            MonthDistanceTuple(it.month, it.distance)
        }
    }

    /**
     * Retrieves the total time spent walking for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthTimeTuple objects representing the total time spent walking for each month.
     */
    override fun findTimeForYear(year: String): List<MonthTimeTuple> {
        return walkItems.filter {
            it.date.startsWith(year)
        }.map {
            MonthTimeTuple(it.month, it.time)
        }
    }

    /**
     * Retrieves the total calories burned for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthCaloriesTuple objects representing the total calories burned for each month.
     */
    override fun findCaloriesForYear(year: String): List<MonthCaloriesTuple> {
        return walkItems.filter {
            it.date.startsWith(year)
        }.map {
            MonthCaloriesTuple(it.month, it.calories)
        }
    }

    /**
     * Retrieves the total number of steps taken for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthStepsTuple objects representing the total number of steps taken for each month.
     */
    override fun findStepsForYear(year: String): List<MonthStepsTuple> {
        return walkItems.filter {
            it.date.startsWith(year)
        }.map {
            MonthStepsTuple(it.month, it.steps)
        }
    }

    /**
     * Retrieves the average speed for each month of a given year.
     *
     * @param year The year for which to retrieve the data.
     * @return A list of MonthSpeedTuple objects representing the average speed for each month.
     */
    override fun findSpeedForYear(year: String): List<MonthSpeedTuple> {
        return walkItems.filter {
            it.date.startsWith(year)
        }.map {
            MonthSpeedTuple(it.month, it.averageSpeed)
        }
    }

    /**
     * Inserts or updates a new walk in the database.
     *
     * @param newWalkEntity The walk entity to insert or update.
     * @return The ID of the inserted or updated walk.
     */
    override suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long {
        walkItems.add(newWalkEntity)
        return (walkItems.size-1).toLong()
    }

    /**
     * Inserts or updates a new path point in the database.
     *
     * @param newPathPointEntity The path point entity to insert or update.
     */
    override suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity) {
        pathPointItems.add(newPathPointEntity)
    }
}
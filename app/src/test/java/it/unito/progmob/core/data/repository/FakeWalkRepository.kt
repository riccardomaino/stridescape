package it.unito.progmob.core.data.repository

import it.unito.progmob.core.domain.ext.rangeTo
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
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import java.util.Date

class FakeWalkRepository: WalkRepository {

    private var walkItems = mutableListOf<WalkEntity>()
    private var pathPointItems = mutableListOf<PathPointEntity>()

    // TEST FUNCTIONS
    fun shouldHaveFilledWalkList(shouldHaveFilledList: Boolean) {
        walkItems = if (shouldHaveFilledList) {
            mutableListOf(
                WalkEntity(1, 0, "2024/01/01", 1, 1000, 1000, 1000, 1000, 1.2f),
                WalkEntity(2, 1, "2024/01/02", 1, 2000, 2000, 2000, 2000, 1.4f),
                WalkEntity(3, 2, "2024/01/03", 1, 3000, 3000, 3000, 3000, 2.3f),
                WalkEntity(4, 3, "2024/02/04", 2, 4000, 4000, 4000, 4000, 2f),
                WalkEntity(5, 4, "2024/02/05", 2, 5000, 5000, 5000, 5000, 1.5f),
                WalkEntity(6, 5, "2024/03/06", 3, 6000, 6000, 6000, 6000, 1.7f),
                WalkEntity(7, 6, "2024/03/07", 3, 7000, 7000, 7000, 7000, 1.9f)
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

    fun addWeeklyStepsForTest(): IntArray {
        val startLocalDate = DateUtils.getFirstDateOfWeek()
        val endLocalDate = DateUtils.getCurrentLocalDateTime().date
        val addedValues = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        val newValues = intArrayOf(1000, 2000, 3000, 4000, 5000, 6000, 7000)
        val dateRangeList = startLocalDate.rangeTo(endLocalDate).asIterable().toMutableList()
        dateRangeList.forEachIndexed { index, date ->
            val dayOfWeek = date.dayOfWeek.value - 1
            val month = date.monthNumber
            val dateStr = date.format(DateUtils.defaultFormatter)
            addedValues[index] = newValues[index]
            walkItems.add(WalkEntity(index+10, dayOfWeek, dateStr, month, newValues[index], newValues[index], newValues[index].toLong(), newValues[index], 1.8f))
        }
        return addedValues
    }

    fun addWalkEntitiesReturnPairsForTest(isInt: Boolean, rangeType: RangeType): WalkPairData {
        val currentLocalDate = DateUtils.getCurrentLocalDateTime().date
        val currentMonth = DateUtils.getCurrentMonth()
        val currentYear = DateUtils.getCurrentYear()

        val startLocalDate = when(rangeType){
            RangeType.WEEK -> DateUtils.getFirstDateOfWeek()
            RangeType.MONTH -> DateUtils.getFirstDateOfMonth()
            RangeType.YEAR -> LocalDate(0,0,0)
        }

        val endLocalDate = when(rangeType){
            RangeType.WEEK -> DateUtils.getLastDateOfWeek()
            RangeType.MONTH -> DateUtils.getLastDateOfMonth()
            RangeType.YEAR -> LocalDate(0,0,0)
        }

        val dateRangeToCurrent = startLocalDate.rangeTo(currentLocalDate).asIterable().toMutableList()
        val dateRangeToEnd = startLocalDate.rangeTo(endLocalDate).asIterable().toMutableList()
        val monthRangeToCurrent = (1..currentMonth).toMutableList()
        val monthRangeToEnd = (1..12).toMutableList()

        val newValues = if (rangeType != RangeType.YEAR) {
            IntArray(dateRangeToEnd.size) { index -> index*1000 }
        } else {
            IntArray(monthRangeToEnd.size) { index -> index*1000 }
        }

        if(isInt){
            val intAddedValues = if (rangeType != RangeType.YEAR) {
                IntArray(dateRangeToEnd.size) { 0 }
            } else {
                IntArray(monthRangeToEnd.size) { 0 }
            }


            if(rangeType != RangeType.YEAR){
                dateRangeToCurrent.forEachIndexed { index, date ->
                    val dayOfWeek = date.dayOfWeek.value - 1
                    val month = date.monthNumber
                    val dateStr = date.format(DateUtils.defaultFormatter)
                    intAddedValues[index] = newValues[index]
                    walkItems.add(WalkEntity(index+10, dayOfWeek, dateStr, month, newValues[index], newValues[index], newValues[index].toLong(), newValues[index], newValues[index].toFloat()))
                }
            } else {
                monthRangeToCurrent.forEachIndexed { index, monthNr ->
                    val date = LocalDate(currentYear, monthNr, 1)
                    val dayOfWeek = date.dayOfWeek.value - 1
                    val dateStr = date.format(DateUtils.defaultFormatter)
                    intAddedValues[index] = newValues[index]
                    walkItems.add(WalkEntity(index+10, dayOfWeek, dateStr, monthNr, newValues[index], newValues[index], newValues[index].toLong(), newValues[index], newValues[index].toFloat()))
                }
            }
            return if(rangeType != RangeType.YEAR) {
                WalkPairData.IntWalkPairData(values = dateRangeToEnd.mapIndexed { index, date -> Pair(date, intAddedValues[index]) }.sortedBy { it.first })
            } else {
                WalkPairData.IntWalkPairData(values = monthRangeToEnd.mapIndexed { index, monthNr ->
                    Pair(LocalDate(currentYear, monthNr, 1), intAddedValues[index]) }.sortedBy { it.first }
                )
            }
        }else{
            val floatAddedValues = FloatArray(dateRangeToEnd.size) { 0f }
            dateRangeToCurrent.forEachIndexed { index, date ->
                val dayOfWeek = date.dayOfWeek.value - 1
                val month = date.monthNumber
                val dateStr = date.format(DateUtils.defaultFormatter)
                floatAddedValues[index] = newValues[index].toFloat()
                walkItems.add(WalkEntity(index+10, dayOfWeek, dateStr, month, newValues[index], newValues[index], newValues[index].toLong(), newValues[index], newValues[index].toFloat()))
            }
            return WalkPairData.FloatWalkPairData(values = dateRangeToEnd.mapIndexed { index, date -> Pair(date, floatAddedValues[index]) }.sortedBy { it.first })
        }
    }

    sealed interface WalkPairData {
        class IntWalkPairData(val values: List<Pair<LocalDate, Int>>) : WalkPairData
        class FloatWalkPairData(val values: List<Pair<LocalDate, Float>>) : WalkPairData
    }



    // IMPLEMENTED INTERFACE FUNCTIONS
    override fun findWalksWithPathPoints(startDate: String, endDate: String): List<WalkWithPathPointsEntity>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { walkEntity ->
                WalkWithPathPointsEntity(
                    walk = walkEntity,
                    pathPoints = pathPointItems.filter { it.walkId.toInt() == walkEntity.id}
                )
            }
        }
    }

    override fun findWalkById(id: Long): WalkEntity? {
        return walkItems.find { it.id?.toLong() == id }
    }

    override fun findPathPointById(id: Long): PathPointEntity? {
        return pathPointItems.find { it.id?.toLong() == id }
    }

    override fun findStepsByDate(date: String): Flow<Int> {
        val result = walkItems.find { it.date == date }?.steps ?: 0
        return flowOf(result)
    }

    override fun findCaloriesByDate(date: String): Flow<Int> {
        val result = walkItems.find { it.date == date }?.calories ?: 0
        return flowOf(result)
    }

    override fun findDistanceByDate(date: String): Flow<Int> {
        val result = walkItems.find { it.date == date }?.distance ?: 0
        return flowOf(result)
    }

    override fun findTimeByDate(date: String): Flow<Long> {
        val result = walkItems.find { it.date == date }?.time ?: 0
        return flowOf(result)
    }

    override fun findStepsBetweenDates(startDate: String, endDate: String): Flow<List<WeekDayStepsTuple>?> {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            flowOf(null)
        } else {
            flowOf(filteredItems.map { WeekDayStepsTuple(it.weekDay, it.steps) })
        }
    }

    override fun findDistanceForDateRange(startDate: String, endDate: String): List<DateDistanceTuple>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { DateDistanceTuple(it.date, it.distance) }
        }
    }

    override fun findTimeForDateRange(startDate: String, endDate: String): List<DateTimeTuple>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { DateTimeTuple(it.date, it.time) }
        }
    }

    override fun findCaloriesForDateRange(startDate: String, endDate: String): List<DateCaloriesTuple>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { DateCaloriesTuple(it.date, it.calories) }
        }
    }

    override fun findStepsForDateRange(startDate: String, endDate: String): List<DateStepsTuple>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { DateStepsTuple(it.date, it.steps) }
        }
    }

    override fun findSpeedForDateRange(startDate: String, endDate: String): List<DateSpeedTuple>? {
        val filteredItems = walkItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { DateSpeedTuple(it.date, it.averageSpeed) }
        }
    }

    override fun findDistanceForYear(year: String): List<MonthDistanceTuple>? {
        val filteredItems = walkItems.filter {it.date.startsWith(year)}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { MonthDistanceTuple(it.month, it.distance) }
        }
    }

    override fun findTimeForYear(year: String): List<MonthTimeTuple>? {
        val filteredItems = walkItems.filter {it.date.startsWith(year)}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { MonthTimeTuple(it.month, it.time) }
        }
    }


    override fun findCaloriesForYear(year: String): List<MonthCaloriesTuple>? {
        val filteredItems = walkItems.filter {it.date.startsWith(year)}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { MonthCaloriesTuple(it.month, it.calories) }
        }
    }

    override fun findStepsForYear(year: String): List<MonthStepsTuple>? {
        val filteredItems = walkItems.filter {it.date.startsWith(year)}
        return if(filteredItems.isEmpty()) {
            null
        } else {
            filteredItems.map { MonthStepsTuple(it.month, it.steps) }
        }
    }

    override fun findSpeedForYear(year: String): List<MonthSpeedTuple> {
        val filteredItems = walkItems.filter {it.date.startsWith(year)}
        return if(filteredItems.isEmpty()) {
            emptyList()
        } else {
            filteredItems.map { MonthSpeedTuple(it.month, it.averageSpeed) }
        }
    }

    override suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long {
        walkItems.add(newWalkEntity)
        return (walkItems.size-1).toLong()
    }

    override suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity) {
        pathPointItems.add(newPathPointEntity)
    }
}
package it.unito.progmob.core.data.repository

import it.unito.progmob.core.domain.ext.rangeTo
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.model.tuples.DateTargetTuple
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.format


class FakeTargetRepository : TargetRepository {

    private var targetItems = mutableListOf<TargetEntity>()

    // TEST FUNCTIONS
    fun shouldHaveFilledList(shouldHaveFilledList: Boolean) {
        targetItems = if (shouldHaveFilledList) {
            mutableListOf(
                TargetEntity("2024/01/01", 1000),
                TargetEntity("2024/01/02", 2000),
                TargetEntity("2024/01/03", 3000),
                TargetEntity("2024/02/04", 4000),
                TargetEntity("2024/02/05", 5000),
                TargetEntity("2024/03/06", 6000),
                TargetEntity("2024/03/07", 7000)
            )
        } else {
            mutableListOf()
        }
    }

    fun addCurrentDayTargetForTest(stepsTarget: Int): Int {
        val formattedCurrentDate = DateUtils.formattedCurrentDate()
        val targetEntity = TargetEntity(formattedCurrentDate, stepsTarget)
        targetItems.add(targetEntity)
        return targetEntity.stepsTarget
    }

    fun addWeeklyTargetForTest(): IntArray {
        val startLocalDate = DateUtils.getFirstDateOfWeek()
        val endLocalDate = DateUtils.getCurrentLocalDateTime().date
        val addedValues = intArrayOf(1, 1, 1, 1, 1, 1, 1)
        val newValues = intArrayOf(1000, 2000, 3000, 4000, 5000, 6000, 7000)
        val dateRangeList = startLocalDate.rangeTo(endLocalDate).asIterable().toMutableList()

        dateRangeList.forEachIndexed { index, date ->
            val dateStr = date.format(DateUtils.defaultFormatter)
            addedValues[index] = newValues[index]
            targetItems.add(TargetEntity(dateStr, newValues[index]))
        }
        return addedValues
    }




    // IMPLEMENTED INTERFACE FUNCTIONS
    override fun findTargetByDate(date: String): Flow<Int> {
        return flowOf(
            targetItems.find {
                it.date == date
            }?.stepsTarget ?: 0
        )
    }

    override fun findLastTarget(): Flow<Int> {
        val result = targetItems.lastOrNull()?.stepsTarget ?: 0
        return flowOf(result)
    }

    override fun findTargetBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<DateTargetTuple>?> {
        val filteredItems = targetItems.filter {it.date in startDate..endDate}
        return if(filteredItems.isEmpty()) {
            flowOf(null)
        } else {
            flowOf( filteredItems.map { DateTargetTuple(it.date, it.stepsTarget) })
        }
    }

    override suspend fun upsertNewTarget(newTargetEntity: TargetEntity) {
        targetItems.removeAll { it.date == newTargetEntity.date}
        targetItems.add(newTargetEntity)
    }
}
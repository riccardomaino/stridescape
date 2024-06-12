package it.unito.progmob.core.data.repository

import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.model.tuples.DateTargetTuple
import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class FakeTargetRepository : TargetRepository {

    private var targetItems = mutableListOf<TargetEntity>()

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

    fun addCurrentDayTarget(target: TargetEntity) {
        targetItems.add(target)
    }

    /**
     * Retrieves the step target for a given date.
     *
     * @param date The date for which to retrieve the target.* @return A flow emitting the step target for the given date.
     */
    override fun findTargetByDate(date: String): Flow<Int> {
        return flowOf(
            targetItems.first {
                it.date == date
            }.stepsTarget
        )
    }

    /**
     * Retrieves the most recently set step target.
     *
     * @return A flow emitting the most recently set step target.
     */
    override fun findLastTarget(): Flow<Int> {
        return flowOf(targetItems.last().stepsTarget)
    }

    /**
     * Retrieves the daily step targets for each day within a given date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A flow emitting a list of DateTargetTuple objects representing the daily step targets.
     */
    override fun findTargetBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<List<DateTargetTuple>?> {
        return flowOf(
            targetItems.filter {
                it.date in startDate..endDate
            }.map {
                DateTargetTuple(it.date, it.stepsTarget)
            }
        )
    }

    /**
     * Inserts or updates a new step target in the database.
     *
     * @param newTargetEntity The step target entity to insert or update.
     */
    override suspend fun upsertNewTarget(newTargetEntity: TargetEntity) {
        targetItems.removeAll { it.date == newTargetEntity.date}
        targetItems.add(newTargetEntity)
    }
}
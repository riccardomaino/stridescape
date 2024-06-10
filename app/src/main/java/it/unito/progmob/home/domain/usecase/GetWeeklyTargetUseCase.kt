package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeeklyTargetUseCase(
    private val walkRepository: WalkRepository
) {
    /**
     * Retrieves the daily step targets for each day of the current week.
     *
     * @return A flow emitting an integer array representing the step targets for each day of the week,
     *         starting with Monday at index 0.
     */
    operator fun invoke(): Flow<IntArray> {
        val startDate = DateUtils.formattedFirstDateOfWeek()
        val endDate = DateUtils.formattedCurrentDate()
        val weekDayTargetArray = intArrayOf(1, 1, 1, 1, 1, 1, 1)

        return walkRepository.findTargetBetweenDates(startDate, endDate).map { listWeekDaysTuple ->
            listWeekDaysTuple?.let {
                listWeekDaysTuple.forEach { weekDayTargetTuple ->
                    val dayOfWeek = DateUtils.getCurrentDayOfWeekFromString(weekDayTargetTuple.date)
                    weekDayTargetArray[dayOfWeek] = weekDayTargetTuple.stepsTarget
                }
            }
            weekDayTargetArray
        }
    }
}
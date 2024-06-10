package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeeklyStepsUseCase(
    private val walkRepository: WalkRepository
) {
    /**
     * Retrieves the total number of steps taken for each day of the current week.
     *
     * @return A flow emitting an integer array representing the steps for each day of the week,
     *         starting with Monday at index 0.
     */
    operator fun invoke(): Flow<IntArray> {
        val startDate = DateUtils.formattedFirstDateOfWeek()
        val endDate = DateUtils.formattedCurrentDate()
        val weekDayStepsArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)

        return walkRepository.findStepsBetweenDates(startDate, endDate).map { listWeekDaysTuple ->
            listWeekDaysTuple?.let {
                listWeekDaysTuple.forEach { weekDayStepsTuple ->
                    weekDayStepsArray[weekDayStepsTuple.weekDay] = weekDayStepsTuple.steps
                }
            }
            weekDayStepsArray

        }
    }
}
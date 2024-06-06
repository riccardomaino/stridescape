package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeeklyStepsUseCase(
    private val walkRepository: WalkRepository
) {
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
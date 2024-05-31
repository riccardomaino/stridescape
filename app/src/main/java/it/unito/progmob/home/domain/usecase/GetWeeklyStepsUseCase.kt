package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class GetWeeklyStepsUseCase(
    private val walkRepository: WalkRepository
) {
    operator fun invoke(): Flow<IntArray> {
        val startDate = DateUtils.getFirstDateOfWeek()
        val endDate = DateUtils.getCurrentDateFormatted()
        val dailySteps = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        return walkRepository.findStepsBetweenDates(startDate, endDate).mapNotNull { dailyStepsTuple ->
            dailyStepsTuple?.let {
                dailySteps[dailyStepsTuple.weekDay] = dailyStepsTuple.steps
            }
            dailySteps
        }
    }
}
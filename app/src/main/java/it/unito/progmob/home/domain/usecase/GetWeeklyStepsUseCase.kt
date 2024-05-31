package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow

class GetWeeklyStepsUseCase(
    private val walkRepository: WalkRepository
) {
    operator fun invoke(): Flow<IntArray> {
        val startDate = DateUtils.getFirstDateOfWeek()
        val endDate = DateUtils.getCurrentDate("yyyy/MM/dd")

        return walkRepository.findStepsBetweenDates(startDate, endDate)
    }
}
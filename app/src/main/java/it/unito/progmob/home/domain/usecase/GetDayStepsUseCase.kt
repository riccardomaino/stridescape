package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayStepsUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Retrieves the total number of steps taken for the given date.
     *
     * @param date The date for which to retrieve the steps.
     * @return A flow emitting the total number of steps taken for the given date.
     */
    operator fun invoke(date: String): Flow<Int> {
        return walkRepository.findStepsByDate(date)
    }
}
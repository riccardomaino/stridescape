package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetDateTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    /**
     * Retrieves the daily step target for the given date.
     *
     * @param date The date for which to retrieve the target.
     *@return A flow emitting the step target for the given date.
     */
    operator fun invoke(date: String): Flow<Int> {
        return targetRepository.findTargetByDate(date)
    }
}
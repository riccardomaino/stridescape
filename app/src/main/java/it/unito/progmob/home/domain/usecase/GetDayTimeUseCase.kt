package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayTimeUseCase @Inject constructor(
   private val walkRepository: WalkRepository
) {
    /**
     * Retrieves the total time spent walking for the given date.
     *
     * @param date The date for which to retrieve the time.
     * @return A flow emitting the total time spent walking for the given date, in milliseconds.
     */
    operator fun invoke(date: String): Flow<Long> {
        return walkRepository.findTimeByDate(date)
    }
}
package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayCaloriesUseCase @Inject constructor(
    private val walkRepository: WalkRepository
){
    /**
     * Retrieves the total calories burned for the given date.
     *
     * @param date The date for which to retrieve the calories.
     * @return A flow emitting the total calories burned for the given date.
     */
    operator fun invoke(date: String): Flow<Int> {
        return walkRepository.findCaloriesByDate(date)
    }
}
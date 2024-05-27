package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayCaloriesUseCase @Inject constructor(
    private val walkRepository: WalkRepository
){
    operator fun invoke(date: String): Flow<Int> {
        return walkRepository.findCaloriesByDate(date)
    }
}
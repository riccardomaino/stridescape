package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayStepsUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    operator fun invoke(currentDay: String): Flow<Int> {
        return walkRepository.findStepsByDate(currentDay)
    }
}
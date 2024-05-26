package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetCurrentDayTimeUseCase @Inject constructor(
   private val walkRepository: WalkRepository
) {
    operator fun invoke(currentDay: String, coroutineScope: CoroutineScope): StateFlow<Long> {
        return walkRepository.findTimeByDate(currentDay)
            .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0L)
    }
}
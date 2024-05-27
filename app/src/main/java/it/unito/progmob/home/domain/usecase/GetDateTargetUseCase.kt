package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetDateTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    operator fun invoke(date: String): Flow<Int> {
        return targetRepository.findTargetByDate(date)
    }
}
package it.unito.progmob.profile.domain.usecase

import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository

) {
    operator fun invoke(): Flow<Int> {
        val date = DateUtils.formattedCurrentDate()
        return targetRepository.findTargetByDate(date)
    }
}
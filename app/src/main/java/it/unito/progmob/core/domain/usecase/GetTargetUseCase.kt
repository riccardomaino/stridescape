package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the target for the current date.
 *
 * @param targetRepository The repository for accessing target data.
 * */
class GetTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository

) {
    /**
     * Invokes the use case and returns a Flow of the target for the current date.
     *
     * @return A Flow of the target for the current date.
     */
    operator fun invoke(): Flow<Int> {
        val date = DateUtils.formattedCurrentDate()
        return targetRepository.findTargetByDate(date)
    }
}
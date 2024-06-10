package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.util.DateUtils
import javax.inject.Inject

class UpdateTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    /**
     * Updates the user's step target for the current date.
     *
     * @param target The new step target as a string.
     */
    suspend operator fun invoke(target: String) {
        val date = DateUtils.formattedCurrentDate()
        val targetEntity = TargetEntity(date = date, stepsTarget =  target.toInt())
        targetRepository.upsertNewTarget(targetEntity)
    }
}
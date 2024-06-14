package it.unito.progmob.main.domain.usecase

import it.unito.progmob.core.domain.Constants
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.take
import javax.inject.Inject


class CheckTargetExistUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    /**
     * Checks if a target exists for the given date. If it doesn't exist, it creates a new target
     * based on the last target. If there is no last target, it creates a new target with a default
     * value
     *
     * @param date The date to check.
     */
    suspend operator fun invoke(date: String) {
        targetRepository.findTargetByDate(date).take(1).collect { target ->
            if (target == 0) {
                targetRepository.findLastTarget().take(1).collect { lastTarget ->
                    if (lastTarget == 0) {
                        val targetEntity = TargetEntity(date = date, stepsTarget = Constants.DEFAULT_STEPS_TARGET)
                        targetRepository.upsertNewTarget(targetEntity)
                    } else {
                        val targetEntity = TargetEntity(date = date, stepsTarget = lastTarget)
                        targetRepository.upsertNewTarget(targetEntity)
                    }
                }
            }
        }
    }
}

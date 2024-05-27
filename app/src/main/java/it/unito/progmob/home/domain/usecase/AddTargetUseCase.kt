package it.unito.progmob.home.domain.usecase

import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.home.domain.model.StepsTarget

class AddTargetUseCase(
    private val targetRepository: TargetRepository
) {
    suspend operator fun invoke(newTarget: StepsTarget) {
        val targetEntity = TargetEntity(
            stepsTarget = newTarget.stepsTarget,
            date = newTarget.date
        )
        targetRepository.upsertNewTarget(targetEntity)
    }
}
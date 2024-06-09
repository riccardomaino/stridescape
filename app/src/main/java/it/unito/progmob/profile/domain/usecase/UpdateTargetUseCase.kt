package it.unito.progmob.profile.domain.usecase

import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import it.unito.progmob.core.domain.util.DateUtils
import javax.inject.Inject

class UpdateTargetUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    suspend operator fun invoke(target: String) {
        val date = DateUtils.formattedCurrentDate()
        val targetEntity = TargetEntity(date = date, stepsTarget =  target.toInt())
        targetRepository.upsertNewTarget(targetEntity)
    }
}
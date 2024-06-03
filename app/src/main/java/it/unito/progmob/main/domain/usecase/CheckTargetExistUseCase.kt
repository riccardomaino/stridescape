package it.unito.progmob.main.domain.usecase

import it.unito.progmob.core.domain.Constants
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.repository.TargetRepository
import kotlinx.coroutines.flow.take
import javax.inject.Inject


class CheckTargetExistUseCase @Inject constructor(
    private val targetRepository: TargetRepository
) {
    suspend operator fun invoke(date: String) {
       targetRepository.findTargetByDate(date).take(1).collect { target ->
           if(target == 0) {
               targetRepository.findLastTarget().take(1).collect { lastTarget ->
                   if(lastTarget == 0){
                       val targetEntity = TargetEntity(date = date, stepsTarget = Constants.DEFAULT_STEPS_TARGET)
                       targetRepository.upsertNewTarget(targetEntity)
                   } else {
                       val targetEntity = TargetEntity(date = date, stepsTarget =  lastTarget)
                       targetRepository.upsertNewTarget(targetEntity)
                   }
               }
           }
       }
    }
}

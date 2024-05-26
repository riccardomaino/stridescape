package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.model.WalkWithPathPoints
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.WalkOrder
import it.unito.progmob.core.domain.util.WalkOrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWalksWithPathPointsUseCase(
    private val walksRepository: WalkRepository
) {
    operator fun invoke(
        walkOrder: WalkOrder = WalkOrder.Date(WalkOrderType.Descending)
    ): Flow<List<WalkWithPathPoints>> {
        return walksRepository.getWalksWithPathPoints().map { walks ->
            when (walkOrder.orderType) {
                is WalkOrderType.Ascending -> {
                    when(walkOrder) {
                        is WalkOrder.Date -> walks.sortedBy { it.walk.date }
                        is WalkOrder.Distance -> walks.sortedBy { it.walk.distance }
                        is WalkOrder.Time -> walks.sortedBy { it.walk.time }
                        is WalkOrder.Steps -> walks.sortedBy { it.walk.steps }
                    }
                }
                is WalkOrderType.Descending -> {
                    when(walkOrder) {
                        is WalkOrder.Date -> walks.sortedByDescending { it.walk.date }
                        is WalkOrder.Distance -> walks.sortedByDescending { it.walk.distance }
                        is WalkOrder.Time -> walks.sortedByDescending { it.walk.time }
                        is WalkOrder.Steps -> walks.sortedByDescending { it.walk.steps }
                    }
                }
            }
        }
    }
}
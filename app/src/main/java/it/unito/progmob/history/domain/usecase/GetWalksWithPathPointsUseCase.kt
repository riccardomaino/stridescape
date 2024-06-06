package it.unito.progmob.history.domain.usecase

import it.unito.progmob.core.domain.model.WalkWithPathPoints
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkOrder
import it.unito.progmob.history.domain.model.WalkOrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWalksWithPathPointsUseCase(
    private val walksRepository: WalkRepository
) {
    operator fun invoke(
        walkOrder: WalkOrder = WalkOrder.Date(WalkOrderType.Descending)
    ): Flow<List<WalkWithPathPoints>> {
        val allWalksGrouped = mutableListOf<AllWalksPerDate>()
        var prevWalk: WalkWithPathPoints? = null
        var newWalk: AllWalksPerDate?

//        walksRepository.findWalksWithPathPoints().forEach { walks ->
//            prevWalk.let {
//                newWalk = AllWalksPerDate(walks.walk.date, mutableListOf())
//                newWalk?.walks?.add(walks)
//            } ?: run {
//                if(prevWalk?.walk?.date == walks.walk.date) {
//                    newWalk?.walks?.add(walks)
//                } else {
//                    allWalksGrouped.add(newWalk!!)
//                    newWalk = AllWalksPerDate(walks.walk.date, mutableListOf())
//                    newWalk?.walks?.add(walks)
//                }
//            }
//            prevWalk = walks
//        }
//        Log.d("GetWalksWithPathPointsUseCase", "allWalksGrouped: $allWalksGrouped")
//        return allWalksGrouped
        return walksRepository.findWalksWithPathPoints().map { walks ->
            when (walkOrder.orderType) {
                is WalkOrderType.Ascending -> {
                    when (walkOrder) {
                        is WalkOrder.Date -> walks.sortedBy { it.walk.date }
                        is WalkOrder.Distance -> walks.sortedBy { it.walk.distance }
                        is WalkOrder.Time -> walks.sortedBy { it.walk.time }
                        is WalkOrder.Steps -> walks.sortedBy { it.walk.steps }
                    }
                }

                is WalkOrderType.Descending -> {
                    when (walkOrder) {
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
package it.unito.progmob.history.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkOrder
import it.unito.progmob.history.domain.model.WalkOrderType
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.tracking.domain.model.PathPoint

class GetWalksWithPathPointsUseCase(
    private val walksRepository: WalkRepository
) {
    operator fun invoke(
        walkOrder: WalkOrder = WalkOrder.Date(WalkOrderType.Descending)
    ): MutableList<AllWalksPerDate> {
        val allWalksGrouped = mutableListOf<AllWalksPerDate>()
        var prevWalk: WalkWithPathPoints? = null
        var newWalks: AllWalksPerDate? = null
        var allWalks = walksRepository.findWalksWithPathPoints().map {
            WalkWithPathPoints(
                weekDay = it.walk.weekDay,
                month = it.walk.month,
                date = it.walk.date,
                distance = it.walk.distance,
                time = it.walk.time,
                steps = it.walk.steps,
                calories = it.walk.calories,
                averageSpeed = it.walk.averageSpeed,
                walkId = it.walk.id!!,
                pathPoints = it.pathPoints.map { pathPointEntity ->
                    if(pathPointEntity.pathPoint is PathPoint.LocationPoint) {
                        PathPoint.LocationPoint(
                            lat = pathPointEntity.pathPoint.lat,
                            lng = pathPointEntity.pathPoint.lng,
                            speed = pathPointEntity.pathPoint.speed
                        )
                    } else {
                        PathPoint.EmptyPoint
                    }
                }
            )
        }

        when (walkOrder.orderType) {
            is WalkOrderType.Ascending -> {
                allWalks = when (walkOrder) {
                    is WalkOrder.Date -> allWalks.sortedBy { it.date }
                    is WalkOrder.Distance -> allWalks.sortedBy { it.distance }
                    is WalkOrder.Time -> allWalks.sortedBy { it.time }
                    is WalkOrder.Steps -> allWalks.sortedBy { it.steps }
                }
            }

            is WalkOrderType.Descending -> {
                allWalks = when (walkOrder) {
                    is WalkOrder.Date -> allWalks.sortedByDescending { it.date }
                    is WalkOrder.Distance -> allWalks.sortedByDescending { it.distance }
                    is WalkOrder.Time -> allWalks.sortedByDescending { it.time }
                    is WalkOrder.Steps -> allWalks.sortedByDescending { it.steps }
                }
            }
        }

        allWalks.forEach { walk ->
            prevWalk?.let {
                if (prevWalk?.date != walk.date) {
                    allWalksGrouped.add(newWalks!!)
                    newWalks = AllWalksPerDate(walk.date, mutableListOf())
                }
                newWalks?.walks?.add(walk)
            } ?: run {
                newWalks = AllWalksPerDate(walk.date, mutableListOf())
                newWalks?.walks?.add(walk)
            }
            prevWalk = walk
        }
        newWalks?.let {
            allWalksGrouped.add(it)
        }
        return allWalksGrouped
    }
}
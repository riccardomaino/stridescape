package it.unito.progmob.history.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.tracking.domain.model.PathPoint

class GetWalksWithPathPointsUseCase(
    private val walksRepository: WalkRepository
) {
    /**
     * Retrieves walks with their path points within the given date range.
     *
     * @param startDate The start date of the range (in milliseconds since epoch).
     * @param endDate The end date of the range (in milliseconds since epoch).
     * @return A list of [AllWalksPerDate] objects, each representing walks associated with a specific date.
     */
    operator fun invoke(
        startDate: Long,
        endDate: Long
    ): List<AllWalksPerDate> {

        val formattedStartDate = DateUtils.formatDateFromEpochMillis(startDate)
        val formattedEndDate = DateUtils.formatDateFromEpochMillis(endDate)

        val groupedWalks = mutableListOf<AllWalksPerDate>()
        var prevWalk: WalkWithPathPoints? = null
        var newWalks: AllWalksPerDate? = null

        val allWalks = walksRepository.findWalksWithPathPoints(
            startDate = formattedStartDate,
            endDate = formattedEndDate
        )?.let {
            it.map { walkWithPathPointsEntity ->
                WalkWithPathPoints(
                    weekDay = walkWithPathPointsEntity.walk.weekDay,
                    month = walkWithPathPointsEntity.walk.month,
                    date = walkWithPathPointsEntity.walk.date,
                    distance = walkWithPathPointsEntity.walk.distance,
                    time = walkWithPathPointsEntity.walk.time,
                    steps = walkWithPathPointsEntity.walk.steps,
                    calories = walkWithPathPointsEntity.walk.calories,
                    averageSpeed = walkWithPathPointsEntity.walk.averageSpeed,
                    walkId = walkWithPathPointsEntity.walk.id!!,
                    pathPoints = walkWithPathPointsEntity.pathPoints.map { pathPointEntity ->
                        if (pathPointEntity.pathPoint is PathPoint.LocationPoint) {
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
        } ?: emptyList()


        allWalks.forEach { walk ->
            prevWalk?.let {
                if (prevWalk?.date != walk.date) {
                    groupedWalks.add(newWalks!!)
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
            groupedWalks.add(it)
        }
        return groupedWalks
    }
}
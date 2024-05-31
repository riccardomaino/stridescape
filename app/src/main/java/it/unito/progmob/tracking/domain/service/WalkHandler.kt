package it.unito.progmob.tracking.domain.service

import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.tracking.domain.model.Walk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WalkHandler {
    /**
     * The [MutableStateFlow] object that holds the current state of the walk. It is exposed to a
     * read-only state flow to prevent external modification of the state.
     */
    private val _walk: MutableStateFlow<Walk> = MutableStateFlow(Walk())
    var walk = _walk.asStateFlow()

    private var initialSteps: Int? = null

    fun trackingServiceStopped(){
        _walk.update {
            it.copy(
                isTracking = false,
                pathPoints = emptyList(),
                distanceInMeters = 0,
                speedInKMH = 0.0f,
                steps = 0,
                timeInMillis = 0L
            )
        }
        initialSteps = null
    }

    /**
     * Updates the [Walk] according to the newPathPoint received from flow of the location and
     * the new time received from the flow of the time
     *
     * @param newPathPoint the new [PathPoint] used to update the [Walk]
     * @param time the new time in milliseconds used to update the [Walk]
     * @param pointsAccuracy the accuracy in meters to consider two path points as different
     */
    fun updateWalkPathPointAndTime(
        newPathPoint: PathPoint.LocationPoint,
        time: Long,
        pointsAccuracy: Int = 5
    ) {
         // Update the milliseconds time in the walk state
        _walk.update {
            it.copy(
                timeInMillis = time
            )
        }
        // Update the distance in meters, path points and speed in the walk state
        val previousPathPoint = _walk.value.pathPoints.lastOrNull()
        previousPathPoint?.let { prevPathPoint ->
            if (prevPathPoint is PathPoint.LocationPoint) {
                if(prevPathPoint != newPathPoint){
                    val distanceBetweenPathPoints = WalkUtils.getDistanceBetweenTwoPathPoints(prevPathPoint, newPathPoint)
                    if (distanceBetweenPathPoints > pointsAccuracy) {
                        _walk.update { walkState ->
                            walkState.copy(
                                distanceInMeters = walkState.distanceInMeters + distanceBetweenPathPoints,
                                pathPoints = walkState.pathPoints + newPathPoint,
                                speedInKMH = WalkUtils.convertSpeedToKmH(newPathPoint.speed)
                            )
                        }
                    }
                }
            } else  {
                _walk.update { walkState ->
                    walkState.copy(
                        pathPoints = walkState.pathPoints + newPathPoint,
                        speedInKMH = WalkUtils.convertSpeedToKmH(newPathPoint.speed)
                    )
                }
            }
        } ?: _walk.update { walkState ->
            walkState.copy(
                pathPoints = walkState.pathPoints + newPathPoint,
                speedInKMH = WalkUtils.convertSpeedToKmH(newPathPoint.speed)
            )
        }
    }

    /**
     * Updates the [Walk] according to the isTracking value
     *
     * @param isTracking the new value of the isTracking field to update the [Walk]
     */
    fun updateWalkTracking(isTracking: Boolean){
        _walk.update {
            it.copy(
                isTracking = isTracking
            )
        }
    }

    /**
     * Updates the [Walk] according to the stepsValue received from the step counter sensor
     *
     * @param newSteps the new number of steps taken by the user to update the [Walk]
     */
    fun updateWalkSteps(newSteps: Int) {
        initialSteps?.let { initialSteps ->
            _walk.update {
                it.copy(
                    steps = newSteps - initialSteps
                )
            }
        } ?: run {
            initialSteps = newSteps
        }
    }

    /**
     * Updates the [Walk] adding an empty [PathPoint] in order to know when the user paused.
     * It's useful in order to know when the path should not be drawn between two points
     */
    fun updateWalkPathPointPaused() {
        _walk.update { walkState ->
            val emptyPoint = PathPoint.EmptyPoint
            walkState.copy(
                pathPoints = walkState.pathPoints + emptyPoint,
            )
        }
    }


    companion object {
        private val TAG = WalkHandler::class.java.simpleName
    }
}
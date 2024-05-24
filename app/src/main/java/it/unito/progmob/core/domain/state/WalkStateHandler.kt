package it.unito.progmob.core.domain.state

import it.unito.progmob.core.domain.model.PathPoint
import it.unito.progmob.core.domain.util.WalkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.RoundingMode

class WalkStateHandler {
    /**
     * The [MutableStateFlow] object that holds the current state of the walk. It is exposed to a
     * read-only state flow to prevent external modification of the state.
     */
    private val _walkState: MutableStateFlow<WalkState> = MutableStateFlow(WalkState())
    var walkState = _walkState.asStateFlow()

    private var _lastCompletedWalkState: MutableStateFlow<WalkState> = MutableStateFlow(WalkState())
    val lastWalkState = _lastCompletedWalkState.asStateFlow()

    private var initialSteps: Int? = null

    fun trackingServiceStarted(){
        _walkState.update {
            it.copy(
                pathPoints = emptyList(),
                distanceInMeters = 0,
                speedInKMH = 0.0f,
                steps = 0,
                timeInMillis = 0L
            )
        }
        initialSteps = null
    }

    fun trackingServiceStopped(){
        _lastCompletedWalkState.update {
            it.copy(
                pathPoints = _walkState.value.pathPoints,
                distanceInMeters = _walkState.value.distanceInMeters,
                speedInKMH = _walkState.value.speedInKMH,
                steps = _walkState.value.steps,
                timeInMillis = _walkState.value.timeInMillis
            )
        }
    }


    /**
     * Updates the [WalkState] according to the newPathPoint received from flow of the location and
     * the new time received from the flow of the time
     *
     * @param newPathPoint the new [PathPoint] used to update the [WalkState]
     * @param time the new time in milliseconds used to update the [WalkState]
     * @param pointsAccuracy the accuracy in meters to consider two path points as different
     */
    fun updateWalkStatePathPointAndTime(
        newPathPoint: PathPoint.LocationPoint,
        time: Long,
        pointsAccuracy: Int = 5
    ) {
         // Update the milliseconds time in the walk state
        _walkState.update {
            it.copy(
                timeInMillis = time
            )
        }
        // Update the distance in meters, path points and speed in the walk state
        val previousPathPoint = _walkState.value.pathPoints.lastOrNull()
        previousPathPoint?.let { prevPathPoint ->
            if (prevPathPoint is PathPoint.LocationPoint) {
                if(prevPathPoint != newPathPoint){
                    val distanceBetweenPathPoints = WalkUtils.getDistanceBetweenTwoPathPoints(prevPathPoint, newPathPoint)
                    if (distanceBetweenPathPoints > pointsAccuracy) {
                        _walkState.update { walkState ->
                            walkState.copy(
                                distanceInMeters = walkState.distanceInMeters + distanceBetweenPathPoints,
                                pathPoints = walkState.pathPoints + newPathPoint,
                                speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                                    .setScale(2, RoundingMode.HALF_UP).toFloat()
                            )
                        }
                    }
                }
            } else  {
                _walkState.update {walkState ->
                    walkState.copy(
                        pathPoints = walkState.pathPoints + newPathPoint,
                        speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                            .setScale(2, RoundingMode.HALF_UP).toFloat()
                    )
                }
            }
        } ?: _walkState.update { walkState ->
            walkState.copy(
                pathPoints = walkState.pathPoints + newPathPoint,
                speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP).toFloat()
            )
        }
    }

    /**
     * Updates the [WalkState] according to the isTracking value
     *
     * @param isTracking the new value of the isTracking field to update the [WalkState]
     */
    fun updateWalkStateTracking(isTracking: Boolean){
        _walkState.update {
            it.copy(
                isTracking = isTracking
            )
        }
    }

    /**
     * Updates the [WalkState] according to the stepsValue received from the step counter sensor
     *
     * @param newSteps the new number of steps taken by the user to update the [WalkState]
     */
    fun updateWalkStateSteps(newSteps: Int) {
        initialSteps?.let { initialSteps ->
            _walkState.update {
                it.copy(
                    steps = newSteps - initialSteps
                )
            }
        } ?: {
            initialSteps = newSteps
        }
    }

    /**
     * Updates the [WalkState] adding an empty [PathPoint] in order to know when the user paused.
     * It's useful in order to know when the path should not be drawn between two points
     */
    fun updateWalkStatePathPointPaused() {
        _walkState.update { walkState ->
            val emptyPoint = PathPoint.EmptyPoint
            walkState.copy(
                pathPoints = walkState.pathPoints + emptyPoint,
            )
        }
    }


    companion object {
        private val TAG = WalkStateHandler::class.java.simpleName
    }
}
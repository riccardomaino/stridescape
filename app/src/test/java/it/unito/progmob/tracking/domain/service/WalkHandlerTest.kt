package it.unito.progmob.tracking.domain.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.tracking.domain.model.Walk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WalkHandlerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var walkHandler: WalkHandler

    @Before
    fun setUp() {
        walkHandler = WalkHandler()
    }

    @Test
    fun `update walk with location path point and time, should update the state list of path point and state time`() = runTest {
        val actualPathPoint = PathPoint.LocationPoint(1.0, 1.0, 1.0f)
        val actualTime = 1000L
        val actualWalk = Walk(
            isTrackingStarted = false,
            isTracking = false,
            distanceInMeters = 0,
            timeInMillis= actualTime,
            speedInKMH = WalkUtils.convertSpeedToKmH(actualPathPoint.speed),
            steps = 0,
            pathPoints = listOf(actualPathPoint)
        )
        walkHandler.updateWalkPathPointAndTime(actualPathPoint, actualTime)

        val result = walkHandler.walk.value
        assertThat(result).isEqualTo(actualWalk)
    }

    @Test
    fun `update walk is tracking started, should update the state is tracking started value`() = runTest {
        val actualWalk = Walk(
            isTrackingStarted = true,
            isTracking = false,
            distanceInMeters = 0,
            timeInMillis= 0L,
            speedInKMH = 0f,
            steps = 0,
            pathPoints = emptyList()
        )
        walkHandler.updateWalkIsTrackingStarted(true)

        val result = walkHandler.walk.value
        assertThat(result).isEqualTo(actualWalk)
    }

    @Test
    fun `update walk is tracking, should update the state is tracking value`() = runTest {
        val actualWalk = Walk(
            isTrackingStarted = false,
            isTracking = true,
            distanceInMeters = 0,
            timeInMillis= 0L,
            speedInKMH = 0f,
            steps = 0,
            pathPoints = emptyList()
        )

        walkHandler.updateWalkIsTracking(true)

        val result = walkHandler.walk.value
        assertThat(result).isEqualTo(actualWalk)
    }

    @Test
    fun `update walk steps, should update the steps of the state`() = runTest {
        val afterRebootSteps = 1000
        val actualSteps = 2000
        val actualWalk = Walk(
            isTrackingStarted = false,
            isTracking = false,
            distanceInMeters = 0,
            timeInMillis= 0L,
            speedInKMH = 0f,
            steps = actualSteps-afterRebootSteps,
            pathPoints = emptyList()
        )

        walkHandler.updateWalkSteps(afterRebootSteps)
        walkHandler.updateWalkSteps(actualSteps)

        val result = walkHandler.walk.value
        assertThat(result).isEqualTo(actualWalk)
    }

    @Test
    fun `update walk when paused, should add an empty path point to the state path point list`() = runTest {
        val emptyPathPoint = PathPoint.EmptyPoint
        val actualWalk = Walk(
            isTrackingStarted = false,
            isTracking = false,
            distanceInMeters = 0,
            timeInMillis= 0L,
            speedInKMH = 0f,
            steps = 0,
            pathPoints = listOf(emptyPathPoint)
        )

        walkHandler.updateWalkPathPointPaused()

        val result = walkHandler.walk.value
        assertThat(result).isEqualTo(actualWalk)
    }
}
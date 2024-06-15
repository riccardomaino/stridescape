package it.unito.progmob.core.domain.util

import it.unito.progmob.core.domain.util.WalkUtils.firstLocationPoint
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.tracking.domain.model.PathPoint
import org.junit.Assert.*
import org.junit.Test

class WalkUtilsTest {
    @Test
    fun `convert the speed in ms to kmh, should match 3,6`() {
        val kmh = WalkUtils.convertSpeedToKmH(1.0f)
        assertTrue(kmh == 3.6f)
    }

    @Test
    fun `convert the distance in meters to kilometers, should match 1`() {
        val km = WalkUtils.convertMetersToKm(1000)
        assertTrue(km == 1.0f)
    }

    @Test
    fun `calculate the calories burnt, should match 50`() {
        val calories = WalkUtils.getCaloriesBurnt(100f, 1000)
        assertTrue(calories == 50)
    }

    @Test
    fun `get the average speed in kmh, should match 3,6`() {
        val pathPoints = listOf(
            PathPoint.LocationPoint(45.0, 45.0, 1f),
            PathPoint.LocationPoint(45.0, 46.0, 2f)
        )
        val averageSpeed = WalkUtils.getAverageSpeedInKmH(pathPoints)
        assertTrue(averageSpeed == 1.5f)
    }

    @Test
    fun `format the distance in meters to kilometers, should match 1`() {
        val formattedDistance = WalkUtils.formatDistanceToKm(1000)
        assertTrue(formattedDistance == "1.0")
    }

    @Test
    fun `get the last location point from a list of path points, should match the last location point`() {
        val pathPoints = listOf(
            PathPoint.LocationPoint(45.0, 45.0, 1f),
            PathPoint.LocationPoint(45.0, 46.0, 2f),
            PathPoint.LocationPoint(45.0, 47.0, 3f)
        )
        val lastPathPoint = pathPoints.lastLocationPoint()
        assertTrue(lastPathPoint?.lat == pathPoints.last().lat)
        assertTrue(lastPathPoint?.lng == pathPoints.last().lng)
        assertTrue(lastPathPoint?.speed == pathPoints.last().speed)
    }

    @Test
    fun `get the last location point from an empty list of path points, should match null`() {
        val pathPoints = emptyList<PathPoint>()
        val lastPathPoint = pathPoints.lastLocationPoint()
        assertNull(lastPathPoint)
    }

    @Test
    fun `get the first location point from a list of path points, should match the first location point`() {
        val pathPoints = listOf(
            PathPoint.LocationPoint(45.0, 45.0, 1f),
            PathPoint.LocationPoint(45.0, 46.0, 2f),
            PathPoint.LocationPoint(45.0, 47.0, 3f)
        )
        val firstPathPoint = pathPoints.firstLocationPoint()
        assertTrue(firstPathPoint?.lat == pathPoints.first().lat)
        assertTrue(firstPathPoint?.lng == pathPoints.first().lng)
        assertTrue(firstPathPoint?.speed == pathPoints.first().speed)
    }
}
package it.unito.progmob.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.tracking.domain.model.PathPoint
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * This class contains unit tests for the [WalkDao] class.
 * It uses Hilt to inject dependencies and Room's in-memory database for testing.
 */
@HiltAndroidTest
@SmallTest
class WalkDaoTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var walkDatabase: WalkDatabase
    private lateinit var walkDao: WalkDao

    @Before
    fun setup() {
        hiltRule.inject()
        walkDao = walkDatabase.walkDao
    }

    @After
    fun tearDown() {
        walkDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun upsertNewWalkAndGetWalkById() = runTest {
        val walk = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walkId = walkDao.upsertNewWalk(walk)
        val retrievedWalk = walkDao.findWalkById(walkId)
        assertEquals(retrievedWalk?.date, "2023/11/15")
        assertEquals(retrievedWalk?.steps, 1000)
        assertEquals(retrievedWalk?.calories, 200)
        assertEquals(retrievedWalk?.time, 600000.toLong())
        assertEquals(retrievedWalk?.distance, 1000)
        assertEquals(retrievedWalk?.averageSpeed, 1.4f)
    }

    @Test
    @Throws(Exception::class)
    fun upsertNewPathPointAndGetPathPoint() = runTest {
        val walk = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walkId = walkDao.upsertNewWalk(walk)
        val pathPoint = PathPointEntity(0, walkId, PathPoint.LocationPoint(1.0, 1.0, 2f),)
        val emptyPathPoint = PathPointEntity(1, walkId, PathPoint.EmptyPoint,)
        walkDao.upsertNewPathPoint(pathPoint)
        walkDao.upsertNewPathPoint(emptyPathPoint)
        val retrievedLocationPoint = walkDao.findPathPointById(0)?.pathPoint as PathPoint.LocationPoint
        assertEquals(retrievedLocationPoint.lat , 1.0)
        assertEquals(retrievedLocationPoint.lng, 1.0)
        assertEquals(retrievedLocationPoint.speed, 2f)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWalk() = runTest {
        val walk = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walkId = walkDao.upsertNewWalk(walk)
        val pathPoint = PathPointEntity(0, walkId, PathPoint.LocationPoint(1.0, 1.0, 2f),)
        walkDao.upsertNewPathPoint(pathPoint)

        val walksWithPathPoints = walkDao.findWalksWithPathPoints("2023/11/15", "2023/11/15")
        assertEquals(walksWithPathPoints?.size, 1)
        assertEquals(walksWithPathPoints?.get(0)?.walk?.steps, 1000)
        assertEquals(walksWithPathPoints?.get(0)?.pathPoints?.size, 1)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsByDate() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/15", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)

        val steps = walkDao.findStepsByDate("2023/11/15").first()
        assertEquals(steps, 1500)
    }

    @Test
    @Throws(Exception::class)
    fun getDistanceByDate() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/15", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)

        val distance = walkDao.findDistanceByDate("2023/11/15").first()
        assertEquals(distance, 2000)
    }

    @Test
    @Throws(Exception::class)
    fun getCaloriesByDate() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/15", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)

        val calories = walkDao.findCaloriesByDate("2023/11/15").first()
        assertEquals(calories, 300)
    }

    @Test
    @Throws(Exception::class)
    fun getTimeByDate() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/15", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)

        val time = walkDao.findTimeByDate("2023/11/15").first()
        assertEquals(time, 920000)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsBetweenDates() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 2, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)

        val stepsWalk1 = walkDao.findStepsBetweenDates("2023/11/15", "2023/11/16").first()
        val stepsWalk2 = walkDao.findStepsBetweenDates("2023/11/16", "2023/11/16").first()
        assertEquals(stepsWalk1?.first()?.steps, 1000)
        assertEquals(stepsWalk2?.first()?.steps, 500)
    }

    @Test
    @Throws(Exception::class)
    fun getDistanceForDateRange() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2023/11/16", 1, 1000, 1000, 320000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val distanceWalk1 = walkDao.findDistanceForDateRange("2023/11/15", "2023/11/16")?.first()
        val distanceWalk2 = walkDao.findDistanceForDateRange("2023/11/16", "2023/11/16")?.first()
        assertEquals(distanceWalk1?.distance, 1000)
        assertEquals(distanceWalk2?.distance, 2000)
    }

    @Test
    @Throws(Exception::class)
    fun getTimeForDateRange() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2023/11/16", 1, 1000, 1000, 900000, 100, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val timeWalk1 = walkDao.findTimeForDateRange("2023/11/15", "2023/11/16")?.first()
        val timeWalk2 = walkDao.findTimeForDateRange("2023/11/16", "2023/11/16")?.first()
        assertEquals(timeWalk1?.time, 600000.toLong())
        assertEquals(timeWalk2?.time, 1220000.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun getCaloriesForDateRange() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2023/11/16", 1, 1000, 1000, 900000, 300, averageSpeed = 1f)

        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val caloriesWalk1 = walkDao.findCaloriesForDateRange("2023/11/15", "2023/11/16")?.first()
        val caloriesWalk2 = walkDao.findCaloriesForDateRange("2023/11/16", "2023/11/16")?.first()
        assertEquals(caloriesWalk1?.calories, 200)
        assertEquals(caloriesWalk2?.calories, 400)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsForDateRange() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2023/11/16", 1, 1000, 1000, 900000, 300, averageSpeed = 1f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val stepsWalk1 = walkDao.findStepsForDateRange("2023/11/15", "2023/11/16")?.first()
        val stepsWalk2 = walkDao.findStepsForDateRange("2023/11/16", "2023/11/16")?.first()
        assertEquals(stepsWalk1?.steps, 1000)
        assertEquals(stepsWalk2?.steps, 1500)
    }

    @Test
    @Throws(Exception::class)
    fun getSpeedForDateRange() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 1, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2023/11/16", 1, 1000, 1000, 900000, 300, averageSpeed = 1.2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)
        val speedWalk1 = walkDao.findSpeedForDateRange("2023/11/15", "2023/11/16")?.first()
        val speedWalk2 = walkDao.findSpeedForDateRange("2023/11/16", "2023/11/16")?.first()
        assertEquals(speedWalk1?.averageSpeed, 1.4f)
        assertEquals(speedWalk2?.averageSpeed, 1.1f)
    }

    @Test
    @Throws(Exception::class)
    fun getDistanceForYear() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 4, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2024/12/16", 3, 900, 2000, 500000, 300, averageSpeed = 2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val distanceWalk1 = walkDao.findDistanceForYear("2023")?.first()
        assertEquals(distanceWalk1?.distance, 2000)
    }

    @Test
    @Throws(Exception::class)
    fun getTimeForYear() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 4, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2024/12/16", 3, 900, 2000, 500000, 300, averageSpeed = 2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val timeWalk1 = walkDao.findTimeForYear("2023")?.first()
        assertEquals(timeWalk1?.time, 920000.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun getCaloriesForYear() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 4, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2024/12/16", 3, 900, 2000, 500000, 300, averageSpeed = 2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val caloriesWalk1 = walkDao.findCaloriesForYear("2023")?.first()
        assertEquals(caloriesWalk1?.calories, 300)
    }

    @Test
    @Throws(Exception::class)
    fun getStepsForYear() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 4, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2024/12/16", 3, 900, 2000, 500000, 300, averageSpeed = 2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val stepsWalk1 = walkDao.findStepsForYear("2023")?.first()
        assertEquals(stepsWalk1?.steps, 1500)
    }

    @Test
    @Throws(Exception::class)
    fun getSpeedForYear() = runTest {
        val walk1 = WalkEntity(0, 1, "2023/11/15", 1, 1000, 1000, 600000, 200, averageSpeed = 1.4f)
        val walk2 = WalkEntity(1, 4, "2023/11/16", 1, 500, 1000, 320000, 100, averageSpeed = 1f)
        val walk3 = WalkEntity(2, 2, "2024/12/16", 3, 900, 2000, 500000, 300, averageSpeed = 2f)
        walkDao.upsertNewWalk(walk1)
        walkDao.upsertNewWalk(walk2)
        walkDao.upsertNewWalk(walk3)

        val speedWalk1 = walkDao.findSpeedForYear("2023")?.first()
        assertEquals(speedWalk1?.averageSpeed, 1.2f)
    }
}
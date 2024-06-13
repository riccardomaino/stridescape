package it.unito.progmob.tracking.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddPathPointUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addPathPointUseCase: AddPathPointUseCase
    private lateinit var fakeWalkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        addPathPointUseCase = AddPathPointUseCase(fakeWalkRepository)
    }

    @Test
    fun `add location point to walk, should match match true`() = runTest {
        fakeWalkRepository.shouldHaveFilledPathPointList(false)
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val walkId = 2
        val pathPoint = PathPoint.LocationPoint(1.0, 1.0, 3f)
        addPathPointUseCase(walkId.toLong(), pathPoint)
        val addedPathPoint = fakeWalkRepository.findPathPointById(1)?.pathPoint as PathPoint.LocationPoint
        assertTrue(pathPoint.lat == addedPathPoint.lat)
        assertTrue(pathPoint.lng == addedPathPoint.lng)
        assertTrue(pathPoint.speed == addedPathPoint.speed)
    }

    @Test
    fun `add empty point to walk, should match true`() = runTest {
        fakeWalkRepository.shouldHaveFilledPathPointList(false)
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val walkId = 2
        val pathPoint = PathPoint.EmptyPoint
        addPathPointUseCase(walkId.toLong(), pathPoint)
        val addedPathPoint = fakeWalkRepository.findPathPointById(1)?.pathPoint
        assertTrue(addedPathPoint is PathPoint.EmptyPoint)
    }

}
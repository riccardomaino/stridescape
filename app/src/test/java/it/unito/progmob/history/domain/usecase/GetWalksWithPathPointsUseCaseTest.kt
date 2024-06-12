package it.unito.progmob.history.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.tracking.domain.model.PathPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWalksWithPathPointsUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getWalksWithPathPointsUseCase: GetWalksWithPathPointsUseCase
    private lateinit var fakeWalkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getWalksWithPathPointsUseCase = GetWalksWithPathPointsUseCase(fakeWalkRepository)
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        fakeWalkRepository.shouldHaveFilledPathPointList(true)
    }

    @Test
    fun `get walks with path points, should return 4, 1 and 2`() {
        val result = getWalksWithPathPointsUseCase(1704099665000, 1707037265000)
        assertEquals(4, result.size)
        assertEquals(1, result.first().walks.size)
        assertEquals(2, result.first().walks.first().pathPoints.size)
    }

    @Test
    fun `get walks with path with empty path point list, should return 1`() {
        val result = getWalksWithPathPointsUseCase(1704189665000, 1704189665000)
        assertEquals(1, result.size)
        assertEquals(1, result.first().walks.size)
        assertThat(result.first().walks.first().pathPoints).isEmpty()
    }

    @Test
    fun `get walks with path points with an empty point, should match true`() {
        val result = getWalksWithPathPointsUseCase(1704276065000, 1704276065000)
        assertTrue(result.first().walks.first().pathPoints.last() is PathPoint.EmptyPoint)
    }
}
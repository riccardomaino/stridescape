package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetDayDistanceUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getDayDistanceUseCase: GetDayDistanceUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getDayDistanceUseCase = GetDayDistanceUseCase(fakeWalkRepository)
    }

    @Test
    fun `get day distance with date present, should return 3000`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 3000
        val distance = getDayDistanceUseCase("2024/01/03").first()
        assertEquals(actualValue, distance)
    }

    @Test
    fun `get day distance with date not present, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 0
        val distance = getDayDistanceUseCase("2024/01/05").first()
        assertEquals(actualValue, distance)
    }

    @Test
    fun `get day distance with empty database, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val actualValue = 0
        val distance = getDayDistanceUseCase("2024/01/01").first()
        assertEquals(actualValue, distance)
    }
}
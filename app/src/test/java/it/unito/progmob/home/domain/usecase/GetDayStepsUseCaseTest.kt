package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetDayStepsUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getDayStepsUseCase: GetDayStepsUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getDayStepsUseCase = GetDayStepsUseCase(fakeWalkRepository)
    }

    @Test
    fun `get day steps with date present, should return 1000`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 1000
        val steps = getDayStepsUseCase("2024/01/01").first()
        assertEquals(actualValue, steps)
    }

    @Test
    fun `get day steps with date not present, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 0
        val steps = getDayStepsUseCase("2024/01/05").first()
        assertEquals(actualValue, steps)
    }

    @Test
    fun `get day steps with empty database, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val actualValue = 0
        val steps = getDayStepsUseCase("2024/01/01").first()
        assertEquals(actualValue, steps)
    }

}
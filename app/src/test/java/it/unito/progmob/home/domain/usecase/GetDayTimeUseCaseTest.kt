package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetDayTimeUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getDayTimeUseCase: GetDayTimeUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getDayTimeUseCase = GetDayTimeUseCase(fakeWalkRepository)
    }

    @Test
    fun `get day time with date present, should return 1000`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 4000L
        val time = getDayTimeUseCase("2024/02/04").first()
        assertEquals(actualValue, time)
    }

    @Test
    fun `get day time with date not present, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 0L
        val time = getDayTimeUseCase("2024/01/05").first()
        assertEquals(actualValue, time)
    }

    @Test
    fun `get day time with empty database, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val actualValue = 0L
        val time = getDayTimeUseCase("2024/01/01").first()
        assertEquals(actualValue, time)
    }
}
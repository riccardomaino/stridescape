package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetDayCaloriesUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getDayCaloriesUseCase: GetDayCaloriesUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getDayCaloriesUseCase = GetDayCaloriesUseCase(fakeWalkRepository)
    }

    @Test
    fun `get day calories with date present, should return 2000`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 2000
        val calories = getDayCaloriesUseCase("2024/01/02").first()
        assertEquals(actualValue, calories)
    }

    @Test
    fun `get day calories with date not present, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualValue = 0
        val calories = getDayCaloriesUseCase("2024/01/05").first()
        assertEquals(actualValue, calories)
    }

    @Test
    fun `get day calories with empty database, should return 0`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val actualValue = 0
        val calories = getDayCaloriesUseCase("2024/01/01").first()
        assertEquals(actualValue, calories)
    }
}
package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeeklyStepsUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getWeeklyStepsUseCase: GetWeeklyStepsUseCase


    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getWeeklyStepsUseCase = GetWeeklyStepsUseCase(fakeWalkRepository)
    }

    @Test
    fun `get weekly steps, returns the correct steps for each day of the week`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(true)
        val actualSteps = fakeWalkRepository.addWeeklyStepsForTest()
        val result = getWeeklyStepsUseCase().first()
        assertThat(result).isEqualTo(actualSteps)
    }

    @Test
    fun `get weekly steps with empty database, returns an array of zeros`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val actualSteps = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        val result = getWeeklyStepsUseCase().first()
        assertThat(result).isEqualTo(actualSteps)
    }
}
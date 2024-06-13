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
    fun `get weekly steps, should return the correct steps for each day of the week`() = runTest {
        val data = fakeWalkRepository.addWeekWalkEntitiesForTest(
            fill = true,
            isInt = true
        ) as FakeWalkRepository.WalkData.IntWalkData

        val actual = data.values
        val result = getWeeklyStepsUseCase().first()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get weekly steps with empty database, should return an integer array of zeros`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWeekWalkEntitiesForTest(
            fill = false,
            isInt = true
        ) as FakeWalkRepository.WalkData.IntWalkData

        val actual =  data.values
        val result = getWeeklyStepsUseCase().first()
        assertThat(result).isEqualTo(actual)
    }
}
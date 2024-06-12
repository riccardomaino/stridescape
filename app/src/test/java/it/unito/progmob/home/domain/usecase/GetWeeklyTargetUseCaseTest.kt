package it.unito.progmob.home.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeeklyTargetUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeTargetRepository: FakeTargetRepository
    private lateinit var getWeeklyTargetUseCase: GetWeeklyTargetUseCase

    @Before
    fun setUp() {
        fakeTargetRepository = FakeTargetRepository()
        getWeeklyTargetUseCase = GetWeeklyTargetUseCase(fakeTargetRepository)
    }

    @Test
    fun `get weekly target, returns the correct target for each day of the week`() = runTest {
        fakeTargetRepository.shouldHaveFilledList(true)
        val actualTarget = fakeTargetRepository.addWeeklyTargetForTest()
        val result = getWeeklyTargetUseCase().first()
        assertThat(result).isEqualTo(actualTarget)
    }

    @Test
    fun `get weekly target with empty database, returns an array of ones`() = runTest {
        fakeTargetRepository.shouldHaveFilledList(false)
        val actualTarget = intArrayOf(1, 1, 1, 1, 1, 1, 1)
        val result = getWeeklyTargetUseCase().first()
        assertThat(result).isEqualTo(actualTarget)
    }
}
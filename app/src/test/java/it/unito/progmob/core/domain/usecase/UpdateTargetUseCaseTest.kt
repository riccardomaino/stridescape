package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdateTargetUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var updateTargetUseCase: UpdateTargetUseCase
    private lateinit var fakeTargetRepository: FakeTargetRepository

    @Before
    fun setUp() {
        fakeTargetRepository = FakeTargetRepository()
        updateTargetUseCase = UpdateTargetUseCase(fakeTargetRepository)
        fakeTargetRepository.shouldHaveFilledList(true)
    }

    @Test
    fun `update target, should return 10000`() = runTest{
        val testTarget = 10000
        updateTargetUseCase(testTarget.toString())
        val result = fakeTargetRepository.findLastTarget().first()
        assertEquals(testTarget, result)
    }
}
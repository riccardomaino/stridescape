package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetTargetUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getTargetUseCase: GetTargetUseCase
    private lateinit var fakeTargetRepository: FakeTargetRepository

    @Before
    fun setUp() {
        fakeTargetRepository = FakeTargetRepository()
        getTargetUseCase = GetTargetUseCase(fakeTargetRepository)

        fakeTargetRepository.shouldHaveFilledList(true)
    }

    @Test
    fun `get the last target, should return 9000`() = runTest {
        val currentDate = DateUtils.formattedCurrentDate()
        val target = TargetEntity(currentDate, 9000)
        fakeTargetRepository.addCurrentDayTarget(target)
        val result = getTargetUseCase().first()
        assertEquals(9000, result)
    }
}
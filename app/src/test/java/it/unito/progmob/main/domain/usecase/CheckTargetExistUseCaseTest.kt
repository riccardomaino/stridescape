package it.unito.progmob.main.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckTargetExistUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var checkTargetExistUseCase: CheckTargetExistUseCase
    private lateinit var fakeTargetRepository: FakeTargetRepository

    @Before
    fun setUp() {
        fakeTargetRepository = FakeTargetRepository()
        checkTargetExistUseCase = CheckTargetExistUseCase(fakeTargetRepository)
    }

    @Test
    fun `check target exist when target is already set, should not create new target`() = runTest {
        fakeTargetRepository.shouldHaveFilledList(true)
        checkTargetExistUseCase("2024/03/07")
        val target = fakeTargetRepository.findTargetByDate("2024/03/07").first()
        assertEquals(7000, target)
    }

    @Test
    fun `check target exist when list is empty, should create new target with default value`() = runTest {
        fakeTargetRepository.shouldHaveFilledList(false)
        checkTargetExistUseCase("2024/01/01")
        val target = fakeTargetRepository.findTargetByDate("2024/01/01").first()
        assertEquals(5000, target)
    }

    @Test
    fun `check target exist when list is not empty and it is not set yet, should create new target with last target value`() = runTest {
        fakeTargetRepository.shouldHaveFilledList(true)
        checkTargetExistUseCase("2024/03/08")
        val target = fakeTargetRepository.findTargetByDate("2024/03/08").first()
        assertEquals(7000, target)
    }
}
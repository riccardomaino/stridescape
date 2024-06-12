package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateTargetUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var validateTargetUseCase: ValidateTargetUseCase

    @Before
    fun setUp() {
        validateTargetUseCase = ValidateTargetUseCase()
    }

    @Test
    fun `validate target, should return true`() {
        val testTarget = "10000"
        val result = validateTargetUseCase(testTarget).successful
        assertEquals(true, result)
    }

    @Test
    fun `validate target equal to 0, should return false`() {
        val testTarget = "0"
        val result = validateTargetUseCase(testTarget).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate target lower than 0, should return false`() {
        val testTarget = "-1"
        val result = validateTargetUseCase(testTarget).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate empty target, should return false`() {
        val testTarget = ""
        val result = validateTargetUseCase(testTarget).successful
        assertEquals(false, result)
    }
}
package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateWeightUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var validateWeightUseCase: ValidateWeightUseCase

    @Before
    fun setUp() {
        validateWeightUseCase = ValidateWeightUseCase()
    }

    @Test
    fun `validate weight, should return true`() {
        val testWeight = "70"
        val result = validateWeightUseCase(testWeight).successful
        assertEquals(true, result)
    }

    @Test
    fun `validate weight lower than 15, should return false`() {
        val testWeight = "10"
        val result = validateWeightUseCase(testWeight).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate weight greater than 300, should return false`() {
        val testWeight = "301"
        val result = validateWeightUseCase(testWeight).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate empty weight, should return false`() {
        val testWeight = ""
        val result = validateWeightUseCase(testWeight).successful
        assertEquals(false, result)
    }
}
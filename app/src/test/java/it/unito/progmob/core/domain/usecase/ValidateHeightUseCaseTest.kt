package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateHeightUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var validateHeightUseCase: ValidateHeightUseCase

    @Before
    fun setUp() {
        validateHeightUseCase = ValidateHeightUseCase()
    }
    // Test that the use case validates the height
    @Test
    fun `validate height, should return true`() {
        val testHeight = "180"
        val result = validateHeightUseCase(testHeight).successful
        assertEquals(true, result)
    }

    @Test
    fun `validate height lower than 25, should return false`() {
        val testHeight = "20"
        val result = validateHeightUseCase(testHeight).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate height greater than 250, should return false`() {
        val testHeight = "300"
        val result = validateHeightUseCase(testHeight).successful
        assertEquals(false, result)
    }

    @Test
    fun `validate empty height, should return false`() {
        val testHeight = ""
        val result = validateHeightUseCase(testHeight).successful
        assertEquals(false, result)
    }
}
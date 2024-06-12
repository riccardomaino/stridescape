package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidateUsernameUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var validateUsernameUseCase: ValidateUsernameUseCase

    @Before
    fun setUp() {
        validateUsernameUseCase = ValidateUsernameUseCase()
    }

    @Test
    fun `validate username, should return true`() {
        val testUsername = "test"
        val result = validateUsernameUseCase(testUsername).successful
        assertEquals(true, result)
    }

    @Test
    fun `validate username with special characters, should return true`() {
        val testUsername = "test123!@#"
        val result = validateUsernameUseCase(testUsername).successful
        assertEquals(true, result)
    }

    @Test
    fun `validate empty username, should return false`() {
        val testUsername = ""
        val result = validateUsernameUseCase(testUsername).successful
        assertEquals(false, result)
    }
}

package it.unito.progmob.main.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReadOnboardingEntryUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var readOnboardingEntryUseCase: ReadOnboardingEntryUseCase
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
        readOnboardingEntryUseCase = ReadOnboardingEntryUseCase(dataStoreManager)
    }

    @Test
    fun `read onboarding entry when it is not set yet, should return false`() = runTest {
        val onboardingEntry = readOnboardingEntryUseCase().first()
        assertEquals(false, onboardingEntry)
    }

    @Test
    fun `read onboarding entry when it is set, should return true`() = runTest {
        dataStoreManager.saveOnboardingEntry()
        val onboardingEntry = readOnboardingEntryUseCase().first()
        assertEquals(true, onboardingEntry)
    }
}
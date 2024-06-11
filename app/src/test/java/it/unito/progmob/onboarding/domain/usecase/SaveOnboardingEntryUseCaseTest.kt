package it.unito.progmob.onboarding.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveOnboardingEntryUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var saveOnboardingEntryUseCase: SaveOnboardingEntryUseCase

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
        saveOnboardingEntryUseCase = SaveOnboardingEntryUseCase(dataStoreManager)
    }

    @After
    fun tearDown() {
        dataStoreManager = FakeDataStoreManager()
    }

    // Test that the use case saves the onboarding entry
    @Test
    fun saveOnboardingEntry() = runTest {
        saveOnboardingEntryUseCase()
        val onboardingEntry = dataStoreManager.readOnboardingEntry().first()
        assertEquals(true, onboardingEntry)
    }
}
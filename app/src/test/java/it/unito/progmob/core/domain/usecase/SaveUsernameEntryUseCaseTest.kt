package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveUsernameEntryUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var saveUsernameEntryUseCase: SaveUsernameEntryUseCase

    @Before
    fun setUp() {
        dataStoreManager = FakeDataStoreManager()
        saveUsernameEntryUseCase = SaveUsernameEntryUseCase(dataStoreManager)
    }

    @After
    fun tearDown() {
        dataStoreManager = FakeDataStoreManager()
    }

    // Test that the use case saves the onboarding entry
    @Test
    fun saveUsernameEntry() = runTest {
        saveUsernameEntryUseCase("test")
        val onboardingEntry = dataStoreManager.readUsernameEntry().first()
        assertEquals("test", onboardingEntry)
    }
}
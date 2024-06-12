package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveUserWeightEntryUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDataStoreManager: DataStoreManager
    private lateinit var saveUserWeightEntryUseCase: SaveUserWeightEntryUseCase

    @Before
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        saveUserWeightEntryUseCase = SaveUserWeightEntryUseCase(fakeDataStoreManager)
    }

    @Test
    fun `save user weight entry, should return 75`() = runTest {
        val testWeight = "75"
        saveUserWeightEntryUseCase(testWeight)
        val result = fakeDataStoreManager.readUserWeightEntry().first()
        assertEquals(testWeight, result)
    }
}
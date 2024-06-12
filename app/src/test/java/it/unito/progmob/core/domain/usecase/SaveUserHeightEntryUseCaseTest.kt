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

class SaveUserHeightEntryUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDataStoreManager: DataStoreManager
    private lateinit var saveUserHeightEntryUseCase: SaveUserHeightEntryUseCase

    @Before
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        saveUserHeightEntryUseCase = SaveUserHeightEntryUseCase(fakeDataStoreManager)
    }

    @Test
    fun `save user height entry, should return 180`() = runTest {
        val testHeight = "180"
        saveUserHeightEntryUseCase(testHeight)
        val result = fakeDataStoreManager.readUserHeightEntry().first()
        assertEquals(testHeight, result)
    }
}
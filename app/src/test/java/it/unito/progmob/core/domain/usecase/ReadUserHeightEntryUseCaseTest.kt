package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReadUserHeightEntryUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var readUserHeightEntryUseCase: ReadUserHeightEntryUseCase
    private lateinit var fakeDataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        readUserHeightEntryUseCase = ReadUserHeightEntryUseCase(fakeDataStoreManager)
    }

    @Test
    fun `invoke should return the user height value`() = runTest {
        val userHeight = "180"
        fakeDataStoreManager.saveUserHeightEntry(userHeight)
        val result = readUserHeightEntryUseCase().first()
        assertEquals(userHeight, result)
    }
}
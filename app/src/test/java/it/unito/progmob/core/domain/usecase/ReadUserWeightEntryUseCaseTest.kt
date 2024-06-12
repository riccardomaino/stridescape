package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReadUserWeightEntryUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var readUserWeightEntryUseCase: ReadUserWeightEntryUseCase
    private lateinit var fakeDataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        readUserWeightEntryUseCase = ReadUserWeightEntryUseCase(fakeDataStoreManager)
    }

    @Test
    fun `read user weight entry, should return 70`() = runTest {
        val userWeight = "70"
        fakeDataStoreManager.saveUserWeightEntry(userWeight)
        val result = readUserWeightEntryUseCase().first()
        assertEquals(userWeight, result)
    }
}
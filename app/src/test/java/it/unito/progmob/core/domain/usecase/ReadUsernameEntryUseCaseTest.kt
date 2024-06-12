package it.unito.progmob.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReadUsernameEntryUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var readUsernameEntryUseCase: ReadUsernameEntryUseCase
    private lateinit var fakeDataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        fakeDataStoreManager = FakeDataStoreManager()
        readUsernameEntryUseCase = ReadUsernameEntryUseCase(fakeDataStoreManager)
    }

    @Test
    fun `read username entry, should return true`() = runTest {
        fakeDataStoreManager.saveUsernameEntry("test")
        val result = readUsernameEntryUseCase().first()
        assertEquals("test", result)
    }
}
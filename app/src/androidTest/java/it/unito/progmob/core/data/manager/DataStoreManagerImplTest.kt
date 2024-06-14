package it.unito.progmob.core.data.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreManagerImplTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val dataStoreManager: DataStoreManager = DataStoreManagerImpl(testContext)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun saveAndReadOnboardingEntry() = runTest {
        dataStoreManager.saveOnboardingEntry()
        val result = dataStoreManager.readOnboardingEntry().first()
        assertThat(result).isTrue()
    }

    @Test
    fun saveAndReadUserWeightEntry() = runTest {
        val actual = "75"
        dataStoreManager.saveUserWeightEntry(actual)
        val result = dataStoreManager.readUserWeightEntry().first()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun saveAndReadUserHeightEntry() = runTest {
        val actual = "180"
        dataStoreManager.saveUserHeightEntry(actual)
        val result = dataStoreManager.readUserHeightEntry().first()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun saveAndReadUsernameEntry() = runTest {
        val actual = "John Doe"
        dataStoreManager.saveUsernameEntry(actual)
        val result = dataStoreManager.readUsernameEntry().first()
        assertThat(result).isEqualTo(actual)
    }
}
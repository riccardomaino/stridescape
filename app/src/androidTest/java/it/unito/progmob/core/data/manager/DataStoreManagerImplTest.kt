package it.unito.progmob.core.data.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DataStoreManagerImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val dataStoreManager: DataStoreManager = DataStoreManagerImpl(testContext)

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
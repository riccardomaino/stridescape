package it.unito.progmob.core.data.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

//private const val TEST_DATASTORE_NAME: String = "test_datastore"

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreManagerImplTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
//    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

//    private val testDataStore: DataStore<Preferences> =
//        PreferenceDataStoreFactory.create(
//            scope = testCoroutineScope,
//            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
//        )

    private val dataStoreManager: DataStoreManager = DataStoreManagerImpl(testContext)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun saveAndReadOnboardingEntry() = runTest {
        dataStoreManager.saveOnboardingEntry()
        val result = dataStoreManager.readOnboardingEntry().first()
        assertEquals(true, result)
    }
    @Test
    fun saveAndReadUserWeightEntry() = runTest {
        val testWeight = "75"
        dataStoreManager.saveUserWeightEntry(testWeight)
        val result = dataStoreManager.readUserWeightEntry().first()
        assertEquals(testWeight, result)
    }

    @Test
    fun saveAndReadUserHeightEntry() = runTest {
        val testHeight = "180"
        dataStoreManager.saveUserHeightEntry(testHeight)
        val result = dataStoreManager.readUserHeightEntry().first()
        assertEquals(testHeight, result)
    }

    @Test
    fun saveAndReadUsernameEntry() = runTest {
        val testName = "John Doe"
        dataStoreManager.saveUserNameEntry(testName)
        val result = dataStoreManager.readUsernameEntry().first()
        assertEquals(testName, result)
    }
}
package it.unito.progmob.core.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import it.unito.progmob.core.domain.manager.DataStoreManager
import it.unito.progmob.core.domain.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [DataStoreManager] interface that uses DataStore to manage user preferences.
 *
 * @param context The application context.
 */
class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStoreManager {

    /**
     * Saves the onboarding entry flag to DataStore.
     */
    override suspend fun saveOnboardingEntry() {
        context.datastore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_ENTRY] = true
        }
    }

    /**
     * Reads the onboarding entry flag from DataStore.
     *
     * @return A Flow of booleans representing the onboarding entry flag.
     */
    override fun readOnboardingEntry(): Flow<Boolean> {
        return context.datastore.data.map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_ENTRY] ?: false
        }
    }

    /**
     * Saves the user weight to DataStore.
     */
    override suspend fun saveUserWeight(weight: String) {
        context.datastore.edit { preferences ->
            preferences[PreferencesKeys.USER_WEIGHT] = weight
        }
    }

    /**
     * Reads the user weight from DataStore.
     *
     * @return A Flow of booleans representing the user weight.
     */
    override fun readUserWeight(): Flow<String> {
        return context.datastore.data.map { preferences ->
            preferences[PreferencesKeys.USER_WEIGHT] ?: ""
        }
    }

    /**
     * Saves the user height to DataStore.
     */
    override suspend fun saveUserHeight(height: String) {
        context.datastore.edit { preferences ->
            preferences[PreferencesKeys.USER_HEIGHT] = height
        }
    }


    /**
     * Reads the user height from DataStore.
     *
     * @return A Flow of booleans representing the user height.
     */
    override fun readUserHeight(): Flow<String> {
        return context.datastore.data.map { preferences ->
            preferences[PreferencesKeys.USER_HEIGHT] ?: ""
        }
    }

    /**
     * Saves the user name to DataStore.
     */
    override suspend fun saveUserName(name: String) {
        context.datastore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    /**
     * Reads the user name from DataStore.
     *
     * @return A Flow of booleans representing the user name.
     */
    override fun readUserName(): Flow<String> {
        return context.datastore.data.map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: ""
        }
    }
}

/**
 * Extension property to create a DataStore instance for the application context.
 */
private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_DATASTORE)

/**
 * Object to hold the keys used in DataStore.
 */
object PreferencesKeys {
    val ONBOARDING_ENTRY = booleanPreferencesKey(name = Constants.ONBOARDING_ENTRY)
    val USER_WEIGHT = stringPreferencesKey(name = Constants.USER_WEIGHT)
    val USER_HEIGHT = stringPreferencesKey(name = Constants.USER_HEIGHT)
    val USER_NAME = stringPreferencesKey(name = Constants.USER_NAME)
}



package it.unito.progmob.core.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
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
    private val context: Context
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
}

/**
 * Extension property to create a DataStore instance for the application context.
 */
private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_DATASTORE)

/**
 * Object to hold the keys used in DataStore.
 */
private object PreferencesKeys {
    val ONBOARDING_ENTRY = booleanPreferencesKey(name = Constants.ONBOARDING_ENTRY)
}



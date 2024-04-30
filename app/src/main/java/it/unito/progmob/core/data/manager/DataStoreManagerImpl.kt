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

class DataStoreManagerImpl @Inject constructor(
    private val context: Context
) : DataStoreManager {
    override suspend fun saveOnboardingEntry() {
        context.datastore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_ENTRY] = true
        }
    }

    override fun readOnboardingEntry(): Flow<Boolean> {
        return context.datastore.data.map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_ENTRY] ?: false
        }
    }
}


private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_DATASTORE)

private object PreferencesKeys {
    val ONBOARDING_ENTRY = booleanPreferencesKey(name = Constants.ONBOARDING_ENTRY)
}



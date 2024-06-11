package it.unito.progmob.core.data.manager

import it.unito.progmob.core.data.manager.PreferencesKeys.ONBOARDING_ENTRY
import it.unito.progmob.core.data.manager.PreferencesKeys.USER_HEIGHT_ENTRY
import it.unito.progmob.core.data.manager.PreferencesKeys.USER_NAME_ENTRY
import it.unito.progmob.core.data.manager.PreferencesKeys.USER_WEIGHT_ENTRY
import it.unito.progmob.core.domain.Constants
import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataStoreManager: DataStoreManager {
    private var dataStore: MutableMap<String, String> = mutableMapOf(
        ONBOARDING_ENTRY to "false",
        USER_NAME_ENTRY  to "",
        USER_WEIGHT_ENTRY to "",
        USER_HEIGHT_ENTRY to ""
    )

    override suspend fun saveOnboardingEntry() {
        dataStore[ONBOARDING_ENTRY] = true.toString()
    }

    override fun readOnboardingEntry(): Flow<Boolean> {
        return flowOf(dataStore[ONBOARDING_ENTRY].toBoolean())
    }

    override suspend fun saveUserWeightEntry(weight: String) {
          dataStore[USER_WEIGHT_ENTRY] = weight
    }

    override fun readUserWeightEntry(): Flow<String> {
        return flowOf(dataStore[USER_WEIGHT_ENTRY]!!)
    }

    override suspend fun saveUserHeightEntry(height: String) {
        dataStore[USER_HEIGHT_ENTRY] = height
    }

    override fun readUserHeightEntry(): Flow<String> {
        return flowOf(dataStore[USER_HEIGHT_ENTRY]!!)
    }

    override suspend fun saveUserNameEntry(name: String) {
        dataStore[USER_NAME_ENTRY] = name
    }

    override fun readUsernameEntry(): Flow<String> {
        return flowOf(dataStore[USER_NAME_ENTRY]!!)
    }
}

object PreferencesKeys {
    const val ONBOARDING_ENTRY = Constants.ONBOARDING_ENTRY
    const val USER_NAME_ENTRY = Constants.USER_NAME_ENTRY
    const val USER_WEIGHT_ENTRY = Constants.USER_WEIGHT_ENTRY
    const val USER_HEIGHT_ENTRY = Constants.USER_HEIGHT_ENTRY
}
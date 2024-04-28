package it.unito.progmob.onboarding.domain.usecases

import it.unito.progmob.core.domain.managers.DataStoreManager
import kotlinx.coroutines.flow.Flow

class ReadOnboardingEntry(
    private val dataStoreManager: DataStoreManager
) {
    operator fun invoke(): Flow<Boolean> {
        return dataStoreManager.readOnboardingEntry()
    }
}
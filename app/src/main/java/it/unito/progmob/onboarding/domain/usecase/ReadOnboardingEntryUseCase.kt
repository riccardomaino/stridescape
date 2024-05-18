package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow

class ReadOnboardingEntryUseCase(
    private val dataStoreManager: DataStoreManager
) {
    /**
     * Reads onboarding entry value as a Flow of Boolean. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @return A Flow representing the current state of the onboarding entry in the DataStore
     */
    operator fun invoke(): Flow<Boolean> {
        return dataStoreManager.readOnboardingEntry()
    }
}
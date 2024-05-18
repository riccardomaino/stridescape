package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager

class SaveOnboardingEntryUseCase(
    private val dataStoreManager: DataStoreManager
) {

    /**
     * Saves the onboarding entry value to the preferences DataStore. This function can be invoked
     * using the use case instance itself due to the overload operator `invoke`.
     */
    suspend operator fun invoke() {
        dataStoreManager.saveOnboardingEntry()
    }
}
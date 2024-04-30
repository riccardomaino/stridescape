package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager

class SaveOnboardingEntryUseCase(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke() {
        dataStoreManager.saveOnboardingEntry()
    }
}
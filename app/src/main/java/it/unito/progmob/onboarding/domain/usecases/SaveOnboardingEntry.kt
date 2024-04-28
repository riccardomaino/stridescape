package it.unito.progmob.onboarding.domain.usecases

import it.unito.progmob.core.domain.managers.DataStoreManager

class SaveOnboardingEntry(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke() {
        dataStoreManager.saveOnboardingEntry()
    }
}
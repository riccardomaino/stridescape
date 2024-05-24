package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager

class SaveUserWeightEntryUseCase (
    private val dataStoreManager: DataStoreManager
) {

    /**
     * Saves the user weight value to the preferences DataStore. This function can be invoked
     * using the use case instance itself due to the overload operator `invoke`.
     */
    suspend operator fun invoke(
        weight: String
    ) {
        dataStoreManager.saveUserWeightEntry(weight)
    }
}
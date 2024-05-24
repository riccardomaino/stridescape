package it.unito.progmob.core.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow

class ReadUserWeightEntryUseCase (
    private val dataStoreManager: DataStoreManager
) {
    /**
     * Reads user weight value as a Flow of Boolean. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @return A Flow representing the current state of the user weight in the DataStore
     */
    operator fun invoke(): Flow<String> {
        return dataStoreManager.readUserWeightEntry()
    }
}
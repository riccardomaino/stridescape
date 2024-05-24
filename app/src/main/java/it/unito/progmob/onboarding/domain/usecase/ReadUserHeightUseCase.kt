package it.unito.progmob.onboarding.domain.usecase

import kotlinx.coroutines.flow.Flow
import it.unito.progmob.core.domain.manager.DataStoreManager

class ReadUserHeightUseCase(
    private val dataStoreManager: DataStoreManager
) {
    /**
     * Reads user height value as a Flow of Boolean. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @return A Flow representing the current state of the user height  in the DataStore
     */
    operator fun invoke(): Flow<String> {
        return dataStoreManager.readUserHeight()
    }
}
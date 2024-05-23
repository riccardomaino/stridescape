package it.unito.progmob.onboarding.domain.usecase

import it.unito.progmob.core.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow

class ReadUserNameUseCase (
    private val dataStoreManager: DataStoreManager
) {
    /**
     * Reads user name value as a Flow of Boolean. This function can be invoked using the use
     * case instance itself due to the overload operator `invoke`.
     * @return A Flow representing the current state of the user name in the DataStore
     */
    operator fun invoke(): Flow<String> {
        return dataStoreManager.readUserName()
    }
}
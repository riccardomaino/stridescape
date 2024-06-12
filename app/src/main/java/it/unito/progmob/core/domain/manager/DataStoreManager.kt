package it.unito.progmob.core.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * An interface for managing data persistence using DataStore.
 */
interface DataStoreManager {
    /**
     * Saves a flag indicating that the onboarding process has been completed.
     */
    suspend fun saveOnboardingEntry()

    /**
     * Reads a flow of boolean values indicating whether the onboarding process has been completed.
     *
     * @return A flow of boolean values.
     */
    fun readOnboardingEntry(): Flow<Boolean>

    /**
     * Saves the user's weight.
     *
     * @param weight The user's weight as a string.
     */
    suspend fun saveUserWeightEntry(weight: String)

    /**
     * Reads a flow of strings representing the user's weight.
     *
     * @return A flow of strings.
     */
    fun readUserWeightEntry(): Flow<String>

    /**
     * Saves the user's height.
     *
     * @param height The user's height as a string.
     */
    suspend fun saveUserHeightEntry(height: String)

    /**
     * Reads a flow of strings representing the user's height.
     *
     * @return A flow of strings.
     */
    fun readUserHeightEntry(): Flow<String>

    /**
     * Saves the user's name.
     *
     * @param name The user's name as a string.
     */
    suspend fun saveUsernameEntry(name: String)

    /**
     * Reads a flow of strings representing the user's name.
     *
     * @return A flow of strings.
     */
    fun readUsernameEntry(): Flow<String>
}
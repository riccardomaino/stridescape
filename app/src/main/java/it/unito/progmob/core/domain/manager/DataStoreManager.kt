package it.unito.progmob.core.domain.manager

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveOnboardingEntry()
    fun readOnboardingEntry(): Flow<Boolean>
    suspend fun saveUserWeightEntry(weight: String)
    fun readUserWeightEntry(): Flow<String>
    suspend fun saveUserHeightEntry(height: String)
    fun readUserHeightEntry(): Flow<String>
    suspend fun saveUserNameEntry(name: String)
    fun readUserNameEntry(): Flow<String>
}
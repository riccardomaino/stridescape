package it.unito.progmob.core.domain.manager

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveOnboardingEntry()
    fun readOnboardingEntry(): Flow<Boolean>

    suspend fun saveUserWeight(weight: String)
    fun readUserWeight(): Flow<String>
    suspend fun saveUserHeight(height: String)
    fun readUserHeight(): Flow<String>
    suspend fun saveUserName(name: String)
    fun readUserName(): Flow<String>


}
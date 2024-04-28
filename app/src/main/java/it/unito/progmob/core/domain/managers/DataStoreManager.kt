package it.unito.progmob.core.domain.managers

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveOnboardingEntry()
    fun readOnboardingEntry(): Flow<Boolean>
}
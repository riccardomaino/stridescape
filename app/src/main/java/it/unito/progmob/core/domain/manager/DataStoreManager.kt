package it.unito.progmob.core.domain.manager

import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveOnboardingEntry()
    fun readOnboardingEntry(): Flow<Boolean>
}
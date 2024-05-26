package it.unito.progmob.tracking.domain.manager

import kotlinx.coroutines.flow.Flow

interface TimeTrackingManager {
    fun startTrackingTime(): Flow<Long>
    fun stopTrackingTime()
}
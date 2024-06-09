package it.unito.progmob.tracking.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * Interface for the time tracking manager.
 */
interface TimeTrackingManager {
    fun startTrackingTime(): Flow<Long>
    fun stopTrackingTime()
}
package it.unito.progmob.core.domain.manager

import kotlinx.coroutines.flow.Flow

interface TimeTrackingManager {
    fun startTrackingTime(): Flow<Long>
    fun stopTrackingTime()

    fun pauseTrackingTime()

    fun resumeTrackingTime()
}
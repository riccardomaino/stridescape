package it.unito.progmob.tracking.data.manager

import it.unito.progmob.tracking.domain.manager.TimeTrackingManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

/**
 * Implementation of [TimeTrackingManager] that keeps track of elapsed time.
 */
class TimeTrackingManagerImpl : TimeTrackingManager {
    private var timeElapsed = 0L

    /**
     * Starts tracking time and emits the elapsed time in milliseconds as a [Flow]. The flow emits
     * the elapsed time every second while the coroutine context remains active.
     *
     * @return A [Flow] emitting the elapsed time in milliseconds.
     */
    override fun startTrackingTime(): Flow<Long> {
        return flow {
            while(currentCoroutineContext().isActive){
                delay(1000)
                timeElapsed += 1000L
                emit(timeElapsed)
            }
        }
    }

    /**
     * Resets the elapsed time to zero.
     */
    override fun stopTrackingTime() {
        timeElapsed = 0L
    }
}
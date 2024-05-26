package it.unito.progmob.tracking.data.manager

import it.unito.progmob.tracking.domain.manager.TimeTrackingManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class TimeTrackingManagerImpl : TimeTrackingManager {
    private var timeElapsed = 0L

    override fun startTrackingTime(): Flow<Long> {
        return flow {
            while(currentCoroutineContext().isActive){
                delay(1000)
                timeElapsed += 1000L
                emit(timeElapsed)
            }
        }
    }

    override fun stopTrackingTime() {
        timeElapsed = 0L
    }



    companion object {
        private val TAG = TimeTrackingManagerImpl::class.java.simpleName
    }
}
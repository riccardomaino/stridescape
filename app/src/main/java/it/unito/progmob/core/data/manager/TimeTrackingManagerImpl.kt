package it.unito.progmob.core.data.manager

import android.util.Log
import it.unito.progmob.core.domain.manager.TimeTrackingManager
import it.unito.progmob.core.domain.service.TrackingService
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.isActive

class TimeTrackingManagerImpl : TimeTrackingManager {
    private var timeElapsed = 0L

    override fun startTrackingTime(): Flow<Long> {
        return flow {
            while(currentCoroutineContext().isActive){
                timeElapsed += 1000L
                delay(1000)
                Log.d(TAG, "Elapsed time: $timeElapsed")
                emit(timeElapsed)
            }
        }
    }

    override fun resumeTrackingTime() {
        startTrackingTime()
    }

    override fun pauseTrackingTime() {
        TODO("Not yet implemented")
    }


    override fun stopTrackingTime() {
        timeElapsed = 0L
    }



    companion object {
        private val TAG = Companion::class.java.simpleName.toString()
    }
}
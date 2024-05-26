package it.unito.progmob.tracking.data.manager

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import it.unito.progmob.tracking.domain.service.TrackingService
import javax.inject.Inject

class TrackingServiceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TrackingServiceManager {
    override fun startTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_START.name
            context.startForegroundService(it)
        }
    }

    override fun resumeTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_RESUME.name
            context.startForegroundService(it)
        }
    }


    override fun pauseTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_PAUSE.name
            context.startForegroundService(it)
        }
    }

    override fun stopTrackingService() {
        Intent(context, TrackingService::class.java).also {
            context.stopService(it)
        }
    }
}
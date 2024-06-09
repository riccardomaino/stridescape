package it.unito.progmob.tracking.data.manager

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import it.unito.progmob.tracking.domain.manager.TrackingServiceManager
import it.unito.progmob.tracking.domain.service.TrackingService
import javax.inject.Inject

/**
 * Implementation of [TrackingServiceManager] that interacts with the [TrackingService]. This class
 * provides methods to start, resume, pause, and stop the tracking service by sending corresponding
 * intents.
 *
 * @param context The application context.
 */
class TrackingServiceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): TrackingServiceManager {

    /**
     * Starts the [TrackingService] in foreground.
     */
    override fun startTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_START.name
            context.startForegroundService(it)
        }
    }

    /**
     * Resumes the [TrackingService].
     */
    override fun resumeTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_RESUME.name
            context.startForegroundService(it)
        }
    }

    /**
     * Pauses the [TrackingService].
     */
    override fun pauseTrackingService() {
        Intent(context, TrackingService::class.java).also {
            it.action = TrackingService.Actions.ACTION_PAUSE.name
            context.startForegroundService(it)
        }
    }

    /**
     * Stops the [TrackingService].
     */
    override fun stopTrackingService() {
        Intent(context, TrackingService::class.java).also {
            context.stopService(it)
        }
    }
}
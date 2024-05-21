package it.unito.progmob.core.domain.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import it.unito.progmob.R
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A service that tracks the user's location and displays a notification with the current location.
 * This service uses the [LocationTrackingManager] to track the user's location and displays a notification
 * with the current location.
 */
class LocationTrackingService @Inject constructor(
    private val locationTrackingManager: LocationTrackingManager
) : Service(){

    /**
     * The service uses a [CoroutineScope] to launch a coroutine that collects the location updates
     * from the [LocationTrackingManager] and updates the notification with the current location. The
     * context of the [CoroutineScope] is a combination of a [SupervisorJob] (which if one of the
     * children job fails the other still keep running) with the [Dispatchers.IO] dispatcher (since we
     * are dealing with IO involving operation) to create the context of this service scope
    */
    private val locationTrackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * This method is called during the creation of the service. It allows us to bind our service to
     * another Android component, but in this case we don't need it. So we return null.
     * @return an IBinder object
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Handles the start and stop actions for the service. This method is called when the service is
     * started or stopped. It checks the action of the intent and calls the appropriate method to
     * start or stop the service.
     *
     * @param intent the intent that started the service
     * @param flags additional data about the start request
     * @param startId a unique integer identifying this start request
     * @return the starting mode of the service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.ACTION_START.name -> start()
            Actions.ACTION_STOP.name -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationTrackingServiceScope.cancel()
    }

    /**
     * Starts the location tracking service. This method creates a notification to display the
     * current location and starts a coroutine to collect location updates from the
     * [LocationTrackingManager].
     */
    private fun start(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.location_notification_content_title))
            .setContentText("Tracking started ...")
            .setStyle(NotificationCompat.BigTextStyle())
            .setOngoing(true)

        locationTrackingServiceScope.launch {
            locationTrackingManager.trackLocation(1000L)
                .catch {
                    e -> Log.e(TAG, "Error tracking location", e)
                }
                .onEach { location ->
                    val latitude = location.latitude.toString()
                    val longitude = location.longitude.toString()
                    val updatedNotification = notification
                        .setContentText("Latitude: $latitude, Longitude: $longitude")
                    notificationManager.notify(
                        NOTIFICATION_ID,
                        updatedNotification.build()
                    )
                }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification.build(),
                FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification.build())
        }
    }

    /**
     * Stops the location tracking service. This method removes the notification and stops the
     * service.
     */
    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }

    /**
     * Companion object containing constants used by the service.
     */
    companion object {
        private val TAG = Companion::class.java.simpleName.toString()
        const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Location Tracking Notifications"
        const val NOTIFICATION_ID = 1
    }

    /**
     * Enum containing the actions that can be used to start or stop the service.
     */
    enum class Actions {
        ACTION_START, ACTION_STOP
    }
}
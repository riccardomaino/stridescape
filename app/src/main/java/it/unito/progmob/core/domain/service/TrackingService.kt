package it.unito.progmob.core.domain.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.MainActivity
import it.unito.progmob.R
import it.unito.progmob.core.domain.Constants.LOCATION_TRACKING_INTERVAL
import it.unito.progmob.core.domain.ext.hasAllPermissions
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import it.unito.progmob.core.domain.manager.TimeTrackingManager
import it.unito.progmob.core.domain.model.PathPoint
import it.unito.progmob.core.domain.sensor.MeasurableSensor
import it.unito.progmob.core.domain.sensor.StepCounterSensor
import it.unito.progmob.core.domain.state.WalkStateHandler
import it.unito.progmob.core.domain.util.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * A service that tracks the user's location and displays a notification with the current location.
 * This service uses the [LocationTrackingManager] to track the user's location and displays a notification
 * with the current location.
 */
@AndroidEntryPoint
class TrackingService : Service() {

    /**
     * The [LocationTrackingManager] is field injected by Dagger since a Service is an Android
     * component and we can only use field injection. It is used to track the user's location
     */
    @Inject
    lateinit var locationTrackingManager: LocationTrackingManager

    /**
     * The [TimeTrackingManager] is field injected by Dagger since a Service is an Android
     * component and we can only use field injection. It is used to track the time spent walking.
     */
    @Inject
    lateinit var timeTrackingManager: TimeTrackingManager

    /**
     * The [WalkStateHandler] is field injected by Dagger since a Service is an Android
     * component and we can only use field injection. It is used to handle the state containing the
     * walking state
     */
    @Inject
    lateinit var walkStateHandler: WalkStateHandler

    /**
     * The [StepCounterSensor] object represent the sensor used used to track the number of steps
     * taken by the user.
     */
    @Inject
    @Named("StepCounterSensor")
    lateinit var stepCounterSensor: MeasurableSensor

    /**
     * The service uses a [CoroutineScope] in which to launch various coroutines that will collects
     * the location updates from the [LocationTrackingManager], collects the timer updates from the
     * [TimeTrackingManager], collects the updates the step counter, and finally updates the
     * notification. The context of this [CoroutineScope] will be a combination of a
     * [SupervisorJob], which if one of the children job fails the other still keep running, with
     * the [Dispatchers.IO] dispatcher, since we are dealing with I/O involving operation.
     */
    private lateinit var trackingServiceScope: CoroutineScope


    /**
     * A flag that indicates if the service is resumed or not. This flag is used to check if the
     * service is resumed or not in order to start the service in the correct way.
     */
    private var hasBeenResumed = false


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
        when (intent?.action) {
            Actions.ACTION_START.name -> start()
            Actions.ACTION_PAUSE.name -> pause()
            Actions.ACTION_RESUME.name -> resume()
        }

        // Returning START_STICKY flag, so the service will be restarted if it's killed by the
        // system or killed due to low memory
        return START_STICKY
    }

    /**
     * Handles the destruction of the service. This method is called when the service is destroyed.
     */
    override fun onDestroy() {
        Log.d(TAG, "onDestroy() called")
        stop()
        super.onDestroy()
    }

    /**
     * Starts the location tracking service. This method creates a notification to display the
     * current location and starts a coroutine to collect location updates from the
     * [LocationTrackingManager].
     */
    private fun start() {
        Log.d(TAG, "start(): called")
        // Check if the application has all the permissions needed to start the service
        if (!this.hasAllPermissions()) {
            Log.e(TAG, "start(): Error, there are some permissions which aren't granted")
            return
        }

        // Create a new coroutine scope with a SupervisorJob and the IO dispatcher
        trackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // If the service is not resumed, update the state to the initial value
        if (!hasBeenResumed) {
            walkStateHandler.trackingServiceStarted()
        }

        // Update the walk state isTracking field to true
        walkStateHandler.updateWalkStateTracking(true)

        // Get the NotificationManager used to notify the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the NotificationCompat.Builder used to build all the notifications
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(getString(R.string.notification_content_title))
            .setContentText("Time: 00:00:00")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Steps: 0"))
            .setContentIntent(getPendingIntent())
            .setAutoCancel(false)
            .setOngoing(true)

        // Combine the location and time tracking flows to update the walk state and the notification
        combine(
            locationTrackingManager.startTrackingLocation(LOCATION_TRACKING_INTERVAL),
            timeTrackingManager.startTrackingTime()
        ) { location, time ->
            val newPathPoint = PathPoint.LocationPoint(
                LatLng(location.latitude, location.longitude), location.speed
            )
            // Update the walk state with the new path point and time
            walkStateHandler.updateWalkStatePathPointAndTime(newPathPoint, time)
            // Notify the the updated notification
            val updatedNotification = notification
                .setContentText("Time: ${TimeUtils.formatMillisTime(time)}")
            notificationManager.notify(
                NOTIFICATION_ID, updatedNotification.build()
            )
        }.launchIn(trackingServiceScope)


        // Start listening to the step counter sensor in a new coroutine in the same scope
        trackingServiceScope.launch {
            stepCounterSensor.startListening()
            stepCounterSensor.setOnSensorValueChangedListener { sensorData ->
                val newSteps = sensorData[0].toInt()
                // Update the walk state with the new steps
                walkStateHandler.updateWalkStateSteps(newSteps)
                // Notify the the updated notification
                val updatedNotification = notification
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Steps: ${walkStateHandler.walkState.value.steps}"))
                notificationManager.notify(
                    NOTIFICATION_ID, updatedNotification.build()
                )
            }
        }

        if (!hasBeenResumed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification.build(),
                    FOREGROUND_SERVICE_TYPE_LOCATION or FOREGROUND_SERVICE_TYPE_HEALTH
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID, notification.build(), FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } else {
                startForeground(NOTIFICATION_ID, notification.build())
            }
        }
    }


    private fun resume() {
        Log.d(TAG, "resume() called")
        hasBeenResumed = true
        start()
    }

    private fun pause() {
        Log.d(TAG, "pause() called")
        trackingServiceScope.cancel()
        stepCounterSensor.stopListening()
        // Update the walk state isTracking field to false
        walkStateHandler.updateWalkStateTracking(false)
        // Update the walk state path adding an empty point
        walkStateHandler.updateWalkStatePathPointPaused()
    }

    /**
     * Stops the tracking service. This method removes the notification and stops the service.
     */
    private fun stop() {
        Log.d(TAG, "stop() called")
        trackingServiceScope.cancel()
        timeTrackingManager.stopTrackingTime()
        stepCounterSensor.stopListening()
        hasBeenResumed = false
        walkStateHandler.updateWalkStateTracking(false)
        walkStateHandler.updateWalkStatePathPointPaused()
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }

    private fun getPendingIntent(): PendingIntent {
        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(Intent(this@TrackingService, MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)!!
        }
    }

    /**
     * This method is called during the creation of the service. It allows us to bind our service to
     * another Android component, but in this case we don't need it. So we return null.
     * @return an IBinder object
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Companion object containing constants used by the service.
     */
    companion object {
        private val TAG = TrackingService::class.java.simpleName
        const val NOTIFICATION_CHANNEL_ID = "location_tracking_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Tracking notifications"
        const val NOTIFICATION_ID = 1
    }

    /**
     * Enum containing the actions that can be used to start or stop the service.
     */
    enum class Actions {
        ACTION_START, ACTION_PAUSE, ACTION_RESUME
    }
}
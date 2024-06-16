package it.unito.progmob.tracking.domain.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.hardware.Sensor
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.R
import it.unito.progmob.core.domain.Constants.LOCATION_TRACKING_INTERVAL
import it.unito.progmob.core.domain.Constants.TRACKING_DEEP_LINK
import it.unito.progmob.core.domain.ext.hasAllPermissions
import it.unito.progmob.core.domain.sensor.AccelerometerSensor
import it.unito.progmob.core.domain.sensor.MeasurableSensor
import it.unito.progmob.core.domain.sensor.StepCounterSensor
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.main.MainActivity
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import it.unito.progmob.tracking.domain.manager.TimeTrackingManager
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A service that tracks the user's location and displays a notification with the current location.
 * This service uses the [LocationTrackingManager] to track the user's location, the
 * [TimeTrackingManager] to track the time spent walking, the [StepCounterSensor] (or
 * [AccelerometerSensor]) to track the number of steps. It displays a notification with the current
 * number of steps and the time spent walking.
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
     * The [WalkHandler] is field injected by Dagger since a Service is an Android
     * component and we can only use field injection. It is used to handle the state containing the
     * walking state
     */
    @Inject
    lateinit var walkHandler: WalkHandler

    /**
     * The [StepCounterSensor] object represent the sensor used used to track the number of steps
     * taken by the user.
     */
    @Inject
    @Named("StepCounterSensor")
    lateinit var stepCounterSensor: MeasurableSensor

    /**
     * The [AccelerometerSensor] object represent the sensor used used to track the number of steps
     * taken by the user if the step counter sensor is not available.
     */
    @Inject
    @Named("AccelerometerSensor")
    lateinit var accelerometerSensor: MeasurableSensor

    /**
     * The number of steps taken by the user. This variable is used to calculate the number of steps
     * using the accelerometer sensor
     */
    private var accelerometerStepCounter = 0

    /**
     * The previous magnitude of the accelerometer sensor. This variable is used to calculate the
     * number of steps using the accelerometer sensor
     */
    private var previousMagnitude = 0.0

    /**
     * The actual sensor used to track the number of steps taken by the user.
     */
    private lateinit var stepSensor: MeasurableSensor

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
     * The [PendingIntent] used to open the app when the notification is clicked.
     */
    private lateinit var pendingIntent: PendingIntent


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
        stop()
        super.onDestroy()
    }

    /**
     * Starts the location tracking service. This method creates a notification to display the
     * current location and starts a coroutine to collect location updates from the
     * [LocationTrackingManager], timer updates from the [TimeTrackingManager], and updates from the
     * step counter
     */
    private fun start() {
        // Check if the application has all the permissions needed to start the service
        if (!this.hasAllPermissions()) {
            return
        }

        // Create a new coroutine scope with a SupervisorJob and the IO dispatcher
        trackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        stepSensor = if (stepCounterSensor.doesSensorExists) {
            stepCounterSensor
        } else {
            accelerometerSensor
        }

        // Update the walk state isTracking field to true
        if (!hasBeenResumed) {
            pendingIntent = getPendingIntent()
            walkHandler.updateWalkIsTrackingStarted(true)
        }
        walkHandler.updateWalkIsTracking(true)

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
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)

        // Combine the location and time tracking flows to update the walk state and the notification
        combine(
            locationTrackingManager.startTrackingLocation(LOCATION_TRACKING_INTERVAL),
            timeTrackingManager.startTrackingTime()
        ) { location, time ->
            val newPathPoint = PathPoint.LocationPoint(
                lat = location.latitude,
                lng = location.longitude,
                speed = WalkUtils.convertSpeedToKmH(location.speed)
            )
            // Update the walk state with the new path point and time
            walkHandler.updateWalkPathPointAndTime(newPathPoint, time)
            // Notify the the updated notification
            val updatedNotification = notification
                .setContentText("Time: ${TimeUtils.formatMillisTime(time)}")
            notificationManager.notify(
                NOTIFICATION_ID, updatedNotification.build()
            )
        }.launchIn(trackingServiceScope)


        // Start listening to the step counter sensor in a new coroutine in the same scope
        trackingServiceScope.launch {
            stepSensor.startListening()
            if (stepSensor.sensorType == Sensor.TYPE_STEP_COUNTER) {
                stepSensor.setOnSensorValueChangedListener { sensorData ->
                    // Get the new steps from the sensor
                    val newSteps = sensorData[0].toInt()
                    // Update the walk state with the new steps
                    walkHandler.updateWalkSteps(newSteps)
                    // Notify the the updated notification
                    val updatedNotification = notification
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText("Steps: ${walkHandler.walk.value.steps}")
                        )
                    notificationManager.notify(
                        NOTIFICATION_ID, updatedNotification.build()
                    )
                }
            } else {
                accelerometerSensor.setOnSensorValueChangedListener { values ->
                    val x: Float = values[0]
                    val y: Float = values[1]
                    val z: Float = values[2]

                    val magnitude = sqrt(x.pow(2.0f) + y.pow(2.0f) + z.pow(2.0f))

                    val magnitudeDelta = magnitude - previousMagnitude
                    previousMagnitude = magnitude.toDouble()

                    if (magnitudeDelta > 6) { // adjust the threshold based on your device
                        accelerometerStepCounter++
                        // Update the walk state with the new steps
                        walkHandler.updateWalkSteps(accelerometerStepCounter, true)
                        // Notify the the updated notification
                        val updatedNotification = notification
                            .setStyle(
                                NotificationCompat.BigTextStyle()
                                    .bigText("Steps: ${walkHandler.walk.value.steps}")
                            )
                        notificationManager.notify(
                            NOTIFICATION_ID, updatedNotification.build()
                        )
                    }
                }
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

    /**
     * Resumes the tracking service. This method starts the location updates and the step counter sensor.
     */
    private fun resume() {
        hasBeenResumed = true
        start()
    }

    /**
     * Pauses the tracking service. This method stops the location updates and the step counter sensor.
     */
    private fun pause() {
        trackingServiceScope.cancel()
        stepSensor.stopListening()
        previousMagnitude = 0.0
        // Update the walk state isTracking field to false
        walkHandler.updateWalkIsTracking(false)
        // Update the walk state path adding an empty point
        walkHandler.updateWalkPathPointPaused()
    }

    /**
     * Stops the tracking service. This method removes the notification and stops the service.
     */
    private fun stop() {
        trackingServiceScope.cancel()
        timeTrackingManager.stopTrackingTime()
        stepSensor.stopListening()
        previousMagnitude = 0.0
        accelerometerStepCounter = 0
        walkHandler.updateWalkIsTracking(false)
        walkHandler.updateWalkIsTrackingStarted(false)
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }

    /**
     * Returns a PendingIntent that opens the app when the notification is clicked.
     * @return a PendingIntent object
     */
    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            TRACKING_DEEP_LINK.toUri(),
            applicationContext,
            MainActivity::class.java
        )
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
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
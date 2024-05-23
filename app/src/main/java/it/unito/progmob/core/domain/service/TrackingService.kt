package it.unito.progmob.core.domain.service

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.MainActivity
import it.unito.progmob.R
import it.unito.progmob.core.domain.manager.LocationTrackingManager
import it.unito.progmob.core.domain.manager.TimeTrackingManager
import it.unito.progmob.core.domain.model.PathPoint
import it.unito.progmob.core.domain.state.WalkState
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.domain.stepscounter.MeasurableSensor
import it.unito.progmob.tracking.domain.stepscounter.StepCounterSensor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

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
     * The [StepCounterSensor] object represent the sensor used used to track the number of steps
     * taken by the user.
     */
    @Inject
    lateinit var stepCounter: MeasurableSensor

    /**
     * The service uses a [CoroutineScope] to launch a coroutine that collects the location updates
     * from the [LocationTrackingManager] and updates the notification with the current location. The
     * context of the [CoroutineScope] is a combination of a [SupervisorJob] (which if one of the
     * children job fails the other still keep running) with the [Dispatchers.IO] dispatcher (since we
     * are dealing with IO involving operation) to create the context of this service scope
     */
    private val trackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    /**
     * The [MutableStateFlow] object that holds the current state of the walk. It is exposed to a
     * read-only state flow to prevent external modification of the state.
     */
    private val _walkState = MutableStateFlow(WalkState())
    val walkState = _walkState.asStateFlow()


    /**
     * The initial number of steps measured by the sensor when the service is started. We have to
     * take in account that in order to compute the correct number of steps since that the step
     * counter sensor is a cumulative sensor that counts the number of steps since the device was
     * booted.
     */
    private var initialSteps: Int = 0


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
        when (intent?.action) {
            Actions.ACTION_START.name -> start()
            Actions.ACTION_PAUSE.name -> pause()
            Actions.ACTION_RESUME.name -> resume()
        }

        // Returning the flag START_STICKY means that the service will be restarted if it is killed
        // by the system or killed due to low memory
        return START_STICKY
        // return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "Fun onDestroy() called")
        stepCounter.stopListening()
        trackingServiceScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
        super.onDestroy()
    }

    /**
     * Starts the location tracking service. This method creates a notification to display the
     * current location and starts a coroutine to collect location updates from the
     * [LocationTrackingManager].
     */
    private fun start() {
        var initialSteps: Int = 0
        if (!checkPermissions()) {
            Log.e(TAG, "Permissions not granted")
            stop()
            return
        }

        _walkState.update {
            it.copy(
                isTracking = true
            )
        }

        // Getting the notification manager to display the notification and creating the
        // NotificationCompat.Builder used to build all the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(getString(R.string.notification_content_title))
            .setContentText("Time: 00:00:00 | Steps: 0")
            .setContentIntent(getTrackingNotificationPendingIntent())
            .setAutoCancel(false)
            .setOngoing(true)

        combine(
            locationTrackingManager.startTrackingLocation(5000L),
            timeTrackingManager.startTrackingTime()
        ) { location, time ->
            val newPathPoint = PathPoint(location.latitude, location.longitude, location.speed)
            updateWalkingState(newPathPoint, time)
            val updatedNotification = notification
                .setContentText("Time: ${TimeUtils.formatMillisTime(time, "hh:mm:ss")} | Steps: ${_walkState.value.steps}")
            notificationManager.notify(
                NOTIFICATION_ID,
                updatedNotification.build()
            )
        }.launchIn(trackingServiceScope)

        trackingServiceScope.launch {
            stepCounter.startListening()
            stepCounter.setOnSensorValueChangedListener { sensorData ->
                val stepsValue = sensorData[0].toInt()
                if (initialSteps == 0){
                    initialSteps = stepsValue
                }
                _walkState.update {
                    it.copy(
                        steps = stepsValue - initialSteps
                    )
                }
                val updatedNotification = notification
                    .setContentText("Time: ${TimeUtils.formatMillisTime(_walkState.value.timeInMillis, "hh:mm:ss")} | Steps: ${_walkState.value.steps}")
                notificationManager.notify(
                    NOTIFICATION_ID,
                    updatedNotification.build()
                )
            }
        }


//        locationTrackingManager.startTrackingLocation(1000L)
//            .catch {
//                e -> Log.e(TAG, "Error during tracking: ", e)
//            }
//            .onEach { location ->
//                val newPathPoint = PathPoint(location.latitude, location.longitude)
//                addPathPoint(newPathPoint)
//                val latitude = location.latitude.toString()
//                val longitude = location.longitude.toString()
//                Log.d(TAG, "LatestPoint: $latitude, $longitude")
//                val updatedNotification = notification
//                    .setContentText("Latitude: $latitude, Longitude: $longitude")
//                notificationManager.notify(
//                    NOTIFICATION_ID,
//                    updatedNotification.build()
//                )
//            }.launchIn(trackingServiceScope)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification.build(),
                FOREGROUND_SERVICE_TYPE_LOCATION or FOREGROUND_SERVICE_TYPE_HEALTH
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification.build(),
                FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification.build())
        }
    }

    private fun resume() {
        start()
    }

    private fun pause() {
        stepCounter.stopListening()
        trackingServiceScope.cancel()
        _walkState.update {
            it.copy(
                isTracking = false
            )
        }
    }

    /**
     * Stops the location tracking service. This method removes the notification and stops the
     * service.
     */
    private fun stop() {
        Log.d(TAG, "Fun stop() called")
        stepCounter.stopListening()
        timeTrackingManager.stopTrackingTime()
//        locationTrackingManager.stopTrackingLocation()
        trackingServiceScope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }

    /**
     * Updates the [WalkState] according to the newPathPoint received from flow of the location
     *
     * @param newPathPoint the new [PathPoint] used to update the [WalkState]
     */
    private fun updateWalkingState(newPathPoint: PathPoint, time: Long, pathPointsAccuracy: Int = 5) {
        // Update the milliseconds time in the walk state
        _walkState.update {
            it.copy(
                timeInMillis = time
            )
        }
        // Update the distance in meters, path points and speed in the walk state
        val previousPathPoint = _walkState.value.pathPoints.lastOrNull()
        previousPathPoint?.let { prevPathPoint ->
            if (prevPathPoint != newPathPoint) {
                val distanceBetweenPathPoints =
                    WalkUtils.getDistanceBetweenTwoPathPoints(prevPathPoint, newPathPoint)
                if (distanceBetweenPathPoints > pathPointsAccuracy) {
                    _walkState.update { walkState ->
                        walkState.copy(
                            distanceInMeters = walkState.distanceInMeters + distanceBetweenPathPoints,
                            pathPoints = walkState.pathPoints + newPathPoint,
                            speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                                .setScale(2, RoundingMode.HALF_UP).toFloat()
                        )
                    }
                    Log.d(TAG, "Added New PathPoint: ${newPathPoint}. New Distance: ${_walkState.value.distanceInMeters}m")
                }
            }
        } ?: _walkState.update { walkState ->
            Log.d(TAG, "Start PathPoint: ${newPathPoint}. First Distance: ${_walkState.value.distanceInMeters}m")
            walkState.copy(
                pathPoints = walkState.pathPoints + newPathPoint,
                speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP).toFloat()
            )
        }
    }


    private fun getTrackingNotificationPendingIntent(): PendingIntent {
        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(Intent(this@TrackingService, MainActivity::class.java))
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)!!
        }
    }

    private fun checkPermissions(): Boolean {
        var hasPermissions = true
        val locationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG, "Access Fine Location permission is not granted")
            hasPermissions = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val activityRecognitionPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            if (activityRecognitionPermission == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "Activity recognition permission is not granted")
                hasPermissions = false
            }
        }
        return hasPermissions
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
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
import it.unito.progmob.core.domain.state.WalkState
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
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
     * The service uses a [CoroutineScope] in which to launch various coroutines that will collects
     * the location updates from the [LocationTrackingManager], collects the timer updates from the
     * [TimeTrackingManager], collects the updates the step counter, and finally updates the
     * notification. The context of this [CoroutineScope] will be a combination of a
     * [SupervisorJob], which if one of the children job fails the other still keep running, with
     * the [Dispatchers.IO] dispatcher, since we are dealing with I/O involving operation.
     */
    private lateinit var trackingServiceScope: CoroutineScope


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
     * A flag that indicates if the service is resumed or not. This flag is used to check if the
     * service is resumed or not in order to start the service in the correct way.
     */
    private var isResumed = false


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

    /**
     * Handles the destruction of the service. This method is called when the service is destroyed.
     */
    override fun onDestroy() {
        Log.d(TAG, "Fun onDestroy() called")
        trackingServiceScope.cancel()
        Log.d(TAG, "Cancelled all coroutines in the trackingServiceScope")
        timeTrackingManager.stopTrackingTime()
        Log.d(TAG, "Resetting the timer to zero")
        stepCounter.stopListening()
        Log.d(TAG, "Stopping listening to step counter sensor")
        initialSteps = 0
        isResumed = false
        updateWalkingStateTracking(false)
        updateWalkingStatePathPaused()
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
        Log.d(TAG, "Fun start() called")
        // Create a new coroutine scope with a SupervisorJob and the IO dispatcher
        trackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // Check if the application has all the permissions needed to start the service
        if (applicationContext.hasAllPermissions()) {
            Log.e(TAG, "There are some permissions which aren't granted")
            stop()
            return
        }

        // Update the walk state setting the isTracking field to true
        updateWalkingStateTracking(true)

        // Get the NotificationManager used to notify the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationCompat.Builder used to build all the notifications
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(getString(R.string.notification_content_title))
            .setContentText("Time: 00:00:00 | Steps: 0")
            .setContentIntent(getTrackingNotificationPendingIntent())
            .setAutoCancel(false)
            .setOngoing(true)


        // Combine the location and time tracking flows to update the walk state and the notification
        combine(
            locationTrackingManager.startTrackingLocation(LOCATION_TRACKING_INTERVAL),
            timeTrackingManager.startTrackingTime()
        ) { location, time ->
            val newPathPoint = PathPoint.LocationPoint(
                LatLng(location.latitude, location.longitude),
                location.speed
            )
            updateWalkingStatePath(newPathPoint, time)
            val updatedNotification = notification
                .setContentText(
                    "Time: ${
                        TimeUtils.formatMillisTime(
                            time,
                            "hh:mm:ss"
                        )
                    } | Steps: ${_walkState.value.steps}"
                )
            notificationManager.notify(
                NOTIFICATION_ID,
                updatedNotification.build()
            )
        }.launchIn(trackingServiceScope)


        // Start listening to the step counter sensor in a new coroutine in the same scope
        trackingServiceScope.launch {
            stepCounter.startListening()
            stepCounter.setOnSensorValueChangedListener { sensorData ->
                val stepsValue = sensorData[0].toInt()
                updateWalkingStateSteps(stepsValue)
                val updatedNotification = notification
                    .setContentText(
                        "Time: ${
                            TimeUtils.formatMillisTime(
                                _walkState.value.timeInMillis,
                                "hh:mm:ss"
                            )
                        } | Steps: ${_walkState.value.steps}"
                    )
                notificationManager.notify(
                    NOTIFICATION_ID,
                    updatedNotification.build()
                )
            }
        }

        if(!isResumed) {
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
            isResumed = true
        }
    }


    private fun resume() {
        Log.d(TAG, "Fun resume() called")
        start()
    }

    private fun pause() {
        Log.d(TAG, "Fun pause() called")
        trackingServiceScope.cancel()
        Log.d(TAG, "Cancelled all coroutines in the trackingServiceScope")
        stepCounter.stopListening()
        Log.d(TAG, "Stopping listening to step counter sensor")
        updateWalkingStateTracking(false)
        updateWalkingStatePathPaused()
    }

    /**
     * Stops the location tracking service. This method removes the notification and stops the
     * service.
     */
    private fun stop() {
        Log.d(TAG, "Fun stop() called")
        trackingServiceScope.cancel()
        timeTrackingManager.stopTrackingTime()
        stepCounter.stopListening()
        initialSteps = 0
        isResumed = false
        updateWalkingStateTracking(false)
        updateWalkingStatePathPaused()
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }


    /**
     * Updates the [WalkState] according to the isTracking value
     *
     * @param isTracking the new value of the isTracking field to update the [WalkState]
     */
    private fun updateWalkingStateTracking(isTracking: Boolean) {
        _walkState.update {
            it.copy(
                isTracking = isTracking
            )
        }
    }

    /**
     * Updates the [WalkState] according to the newPathPoint received from flow of the location
     *
     * @param newPathPoint the new [PathPoint] used to update the [WalkState]
     */
    private fun updateWalkingStatePath(
        newPathPoint: PathPoint.LocationPoint,
        time: Long,
        pathPointsAccuracy: Int = 5
    ) {
        // Update the milliseconds time in the walk state
        _walkState.update {
            it.copy(
                timeInMillis = time
            )
        }
        // Update the distance in meters, path points and speed in the walk state
        val previousPathPoint = _walkState.value.pathPoints.lastOrNull()
        previousPathPoint?.let { prevPathPoint ->
            if (prevPathPoint is PathPoint.LocationPoint) {
                if(prevPathPoint != newPathPoint){
                    val distanceBetweenPathPoints = WalkUtils.getDistanceBetweenTwoPathPoints(prevPathPoint, newPathPoint)
                    if (distanceBetweenPathPoints > pathPointsAccuracy) {
                        _walkState.update { walkState ->
                            walkState.copy(
                                distanceInMeters = walkState.distanceInMeters + distanceBetweenPathPoints,
                                pathPoints = walkState.pathPoints + newPathPoint,
                                speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                                    .setScale(2, RoundingMode.HALF_UP).toFloat()
                            )
                        }
                        Log.d(TAG, "Added CONTINUOUSLY WALKING PathPoint! Lat:${newPathPoint.latLng.latitude} Long:${newPathPoint.latLng.longitude}.")
                        Log.d(TAG,"CONTINUOUSLY WALKING Distance: ${_walkState.value.distanceInMeters}m")
                    }
                }
            } else  {
                _walkState.update {walkState ->
                    Log.d(TAG, "Added NEW START PathPoint! Lat:${newPathPoint.latLng.latitude} Long:${newPathPoint.latLng.longitude}.")
                    walkState.copy(
                        pathPoints = walkState.pathPoints + newPathPoint,
                        speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                            .setScale(2, RoundingMode.HALF_UP).toFloat()
                    )
                }
            }
        } ?: _walkState.update { walkState ->
            Log.d(TAG, "Added START PathPoint! Lat:${newPathPoint.latLng.latitude} Long:${newPathPoint.latLng.longitude}.")
            walkState.copy(
                pathPoints = walkState.pathPoints + newPathPoint,
                speedInKMH = newPathPoint.speed.times(3.6f).toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP).toFloat()
            )
        }
    }

    /**
     * Updates the [WalkState] adding an empty [PathPoint] in order to know when the user paused,
     * useful to not draw and compute the distance during the time that the user paused the tracking.
     */
    private fun updateWalkingStatePathPaused() {
        _walkState.update { walkState ->
            val emptyPoint = PathPoint.EmptyPoint
            walkState.copy(
                pathPoints = walkState.pathPoints + emptyPoint,
            )
        }
    }

    /**
     * Updates the [WalkState] according to the stepsValue received from the step counter sensor
     *
     * @param stepsValue the new number of steps taken by the user to update the [WalkState]
     */
    private fun updateWalkingStateSteps(stepsValue: Int) {
        if (initialSteps == 0) {
            initialSteps = stepsValue
        }
        _walkState.update {
            it.copy(
                steps = stepsValue - initialSteps
            )
        }
    }


    private fun getTrackingNotificationPendingIntent(): PendingIntent {
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
//        val _walkState = MutableStateFlow(WalkState())
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
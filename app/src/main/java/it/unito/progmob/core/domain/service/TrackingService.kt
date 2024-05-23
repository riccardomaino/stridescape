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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * A service that tracks the user's location and displays a notification with the current location.
 * This service uses the [LocationTrackingManager] to track the user's location and displays a notification
 * with the current location.
 */
@AndroidEntryPoint
class TrackingService : Service(){

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
     * The service uses a [CoroutineScope] to launch a coroutine that collects the location updates
     * from the [LocationTrackingManager] and updates the notification with the current location. The
     * context of the [CoroutineScope] is a combination of a [SupervisorJob] (which if one of the
     * children job fails the other still keep running) with the [Dispatchers.IO] dispatcher (since we
     * are dealing with IO involving operation) to create the context of this service scope
    */
    private val trackingServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * The [Job] object that is used to keep track of the coroutine that collects the location
     * updates and the time updates from the [LocationTrackingManager] and the [TimeTrackingManager]
     * respectively.
     */
    private var coroutineJob: Job? = null

    /**
     * The [MutableStateFlow] object that holds the current state of the walk. It is exposed to a
     * read-only state flow to prevent external modification of the state.
     */
    private val _walkState = MutableStateFlow(WalkState())
    val walkState = _walkState.asStateFlow()

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
            Actions.ACTION_PAUSE.name -> pause()
            Actions.ACTION_RESUME.name -> resume()
        }

        // Returning the flag START_STICKY means that the service will be restarted if it is killed
        // by the system or killed due to low memory
        return START_STICKY
        // return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        super.onDestroy()
        trackingServiceScope.cancel()
    }

    /**
     * Starts the location tracking service. This method creates a notification to display the
     * current location and starts a coroutine to collect location updates from the
     * [LocationTrackingManager].
     */
    private fun start(){
        if(!checkPermissions()){
            Log.e(TAG, "Permissions not granted")
            stop()
            return
        }

        // Getting the notification manager to display the notification and creating the
        // NotificationCompatBuild used to build  all the notifications after
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.location_notification_content_title))
            .setContentText("00:00:00")
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(getTrackingNotificationPendingIntent())
            .setAutoCancel(false)
            .setOngoing(true)

        _walkState.update {
            it.copy(
                isTracking = true
            )
        }

        coroutineJob = combine(
            locationTrackingManager.startTrackingLocation(1000L),
            timeTrackingManager.startTrackingTime()
            ){ location, time ->

            val newPathPoint = PathPoint(location.latitude, location.longitude)
            addPathPoint(newPathPoint)

            _walkState.update {
                it.copy(
                    timeInMillis = time,
                )
            }

            val updatedNotification = notification
                .setContentText(TimeUtils.formatMillisTime(time, "HH:mm:ss"))
            notificationManager.notify(
                NOTIFICATION_ID,
                updatedNotification.build()
            )
            Log.d(TAG, "WalkState 'latestPoint': ${_walkState.value.pathPoints.lastOrNull().toString()}")
            Log.d(TAG, "WalkState 'isTracking': ${_walkState.value.isTracking}")
            Log.d(TAG, "WalkState 'timeInMillis': ${_walkState.value.timeInMillis}")
            Log.d(TAG, "WalkState 'speedInKMH': ${_walkState.value.speedInKMH}")
            Log.d(TAG, "WalkState 'distanceInMeters': ${_walkState.value.distanceInMeters}")
            Log.d(TAG, "I'm working in thread ${Thread.currentThread().name}")
        }.launchIn(trackingServiceScope)


//        locationTrackingManager.startTrackingLocation(1000L)
//            .catch {
//                e -> Log.e(TAG, "Error during tracking: ", e)
//            }
//            .onEach { location ->
//                val newPathPoint = PathPoint(location.latitude, location.longitude)
//                addPathPoint(newPathPoint)
//
//                val latitude = location.latitude.toString()
//                val longitude = location.longitude.toString()
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
        } else{
            startForeground(NOTIFICATION_ID, notification.build())
        }
    }

    private fun resume(){
        _walkState.update {
            it.copy(
                isTracking = true
            )
        }
    }

    private fun pause(){
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
    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE) // Immediately remove the notification
        stopSelf() // Stops the service
    }

    /**
     * Adds a new path point to the walk state and updated the distance in meters if and only if
     * there is more than one path point.
     *
     * @param newPathPoint the new [PathPoint] to add
     */
    private fun addPathPoint(newPathPoint: PathPoint){
        val previousPathPoint = _walkState.value.pathPoints.lastOrNull()
        previousPathPoint?.let { prevPathPoint ->
            if(prevPathPoint != newPathPoint){
                _walkState.update { walkState ->
                    walkState.copy(
                        distanceInMeters = WalkUtils.getDistanceBetweenTwoPathPoints(
                            prevPathPoint, newPathPoint
                        ),
                        pathPoints = walkState.pathPoints + newPathPoint
                    )
                }
            }
        } ?: _walkState.update { walkState ->
                walkState.copy(
                    pathPoints = walkState.pathPoints + newPathPoint
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
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(locationPermission == PackageManager.PERMISSION_DENIED){
            Log.e(TAG, "Access Fine Location permission is not granted")
            hasPermissions = false
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val activityRecognitionPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            if(activityRecognitionPermission == PackageManager.PERMISSION_DENIED){
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
        private val TAG = Companion::class.java.simpleName.toString()
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
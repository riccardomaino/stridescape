package it.unito.progmob.tracking.presentation.viewmodel

import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import it.unito.progmob.tracking.domain.model.Walk
import it.unito.progmob.tracking.domain.service.WalkHandler
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import it.unito.progmob.tracking.presentation.TrackingEvent
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class tha handles the tracking feature.
 *
 * @param trackingUseCases The use cases related to the tracking feature.
 * @param walkHandler The handler for managing the state of the walk.
 * @param locationTrackingManager The manager for tracking location updates.
 */
@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingUseCases: TrackingUseCases,
    private val walkHandler: WalkHandler,
    private val locationTrackingManager: LocationTrackingManager
) : ViewModel() {
    // The StateFlow of the WalkState obtained through the WalkHandler
    private val walk: StateFlow<Walk> = walkHandler.walk

    // The tracking feature UI MutableStateFlow which is exposed as a StateFlow to the UI
    private val _uiTrackingState: MutableStateFlow<UiTrackingState> =
        MutableStateFlow(UiTrackingState())
    val uiTrackingState = _uiTrackingState.asStateFlow()

    // The MutableStateFlow used to track if the location provider is enabled. It is exposed as a StateFlow to the UI
    private val _isLocationEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLocationEnabled = _isLocationEnabled.asStateFlow()

    private val _showStopWalkDialog = MutableStateFlow(false)
    val showStopWalkDialog = _showStopWalkDialog.asStateFlow()

    // The user weight used to calculate the calories burnt
    private var userWeight: Float = 0.0f

    // The user height used to calculate the calories burnt
    private var userHeight: Int = 0

    // The last known location of the user used to open the map and to update the camera position when the user click on current location button
    private val _lastKnownLocation = MutableStateFlow<LatLng?>(null)
    val lastKnownLocation = _lastKnownLocation.asStateFlow()

    // The counter of the last known location updates
    private val _lastKnownLocationUpdatesCounter = MutableStateFlow<Long>(0)
    val lastKnownLocationUpdatesCounter = _lastKnownLocationUpdatesCounter.asStateFlow()

    // The job used to collect the walk state
    private var job: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                trackingUseCases.readUserWeightEntryUseCase().take(1).collect {
                    userWeight = it.toFloat()
                }
            }
            launch {
                trackingUseCases.readUserHeightEntryUseCase().take(1).collect {
                    userHeight = it.toInt()
                }
            }
        }

        viewModelScope.launch {
            updateIsLocationEnabledStatus(locationTrackingManager.isConnected())
        }

        viewModelScope.launch {
            trackSingleLocation()
        }
    }

    /**
     * Handles Tracking events emitted from the UI.
     * @param event The TrackingEvent to be processed.
     */
    fun onEvent(event: TrackingEvent) {
        when (event) {
            is TrackingEvent.StartTrackingService -> startTrackingService()
            is TrackingEvent.ResumeTrackingService -> resumeTrackingService()
            is TrackingEvent.PauseTrackingService -> pauseTrackingService()
            is TrackingEvent.StopTrackingService -> stopTrackingService()
            is TrackingEvent.TrackSingleLocation -> trackSingleLocation()
            is TrackingEvent.ShowStopWalkDialog -> showStopWalkDialog(event.showDialog)
            is TrackingEvent.CheckLocationSettings -> checkLocationSettings(event.onEnabled, event.onDisabled)
            is TrackingEvent.UpdateIsLocationEnabledStatus -> updateIsLocationEnabledStatus(event.status)
            is TrackingEvent.StartCollectingState -> startCollectingState()
            is TrackingEvent.StopCollectingState -> stopCollectingState()
        }
    }

    /**
     * Starts collecting the walk state.
     */
    private fun startCollectingState() {
        viewModelScope.launch(Dispatchers.IO) {
            if (job == null) {
                job = walk.onEach { walkState ->
                    _uiTrackingState.update {
                        it.copy(
                            isTrackingStarted = walkState.isTrackingStarted,
                            isTracking = walkState.isTracking,
                            distanceInMeters = walkState.distanceInMeters,
                            timeInMillis = walkState.timeInMillis,
                            speedInKMH = walkState.speedInKMH,
                            pathPoints = walkState.pathPoints,
                            steps = walkState.steps,
                            caloriesBurnt = WalkUtils.getCaloriesBurnt(
                                userWeight,
                                walkState.distanceInMeters
                            )
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    /**
     * Stops collecting the walk state.
     */
    private fun stopCollectingState() {
        viewModelScope.launch {
            job?.cancel()
            job = null
        }
    }

    /**
     * Checks the location settings and invokes the appropriate callback based on the result.
     *
     * @param onEnabled Callback to be invoked if location settings are enabled.
     * @param onDisabled Callback to be invoked if location settings are disabled.
     */
    private fun checkLocationSettings(
        onEnabled: (LocationSettingsResponse) -> Unit,
        onDisabled: (IntentSenderRequest) -> Unit
    ) {
        viewModelScope.launch {
            locationTrackingManager.checkLocationSettings(
                onDisabled = onDisabled,
                onEnabled = onEnabled
            )
        }
    }

    /**
     * Updates the flow used to handle the boolean value of the stop walk dialog based on the
     * parameter passed
     *
     * @param showDialog Whether to show the dialog or not.
     */
    private fun showStopWalkDialog(showDialog: Boolean) {
        viewModelScope.launch {
            _showStopWalkDialog.update { showDialog }
        }
    }

    /**
     * Updates the flow used to handle the boolean value of the location provider status based on the
     * parameter passed
     *
     * @param status The status of the location provider.
     */
    private fun updateIsLocationEnabledStatus(status: Boolean){
        viewModelScope.launch {
            _isLocationEnabled.update { status }
        }
    }

    /**
     * Tracks the last known location of the user. If the tracking is not started, the last known
     * location have to be retrieved from the location provider, otherwise the last known location
     * is the last one recorded.
     */
    private fun trackSingleLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiTrackingState.value.isTracking) {
                _lastKnownLocation.update {
                    _uiTrackingState.value.pathPoints.lastLocationPoint()?.let { lastLocationPoint ->
                        LatLng(lastLocationPoint.lat, lastLocationPoint.lng)
                    }
                }
                _lastKnownLocationUpdatesCounter.update { it + 1 }
            } else {
                trackingUseCases.trackSingleLocationUseCase(onSuccess = { latitude, longitude ->
                        _lastKnownLocation.update { LatLng(latitude, longitude) }
                        _lastKnownLocationUpdatesCounter.update { it + 1 }
                    }
                )
            }
        }
    }

    /**
     * Starts the tracking service.
     */
    private fun startTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.startTrackingUseCase()
        }
    }

    /**
     * Resumes the tracking service.
     */
    private fun resumeTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.resumeTrackingUseCase()
        }
    }

    /**
     * Pauses the tracking service.
     */
    private fun pauseTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.pauseTrackingUseCase()
        }
    }

    /**
     * Stops the tracking service and saves the walk in the database.
     */
    private fun stopTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.stopTrackingUseCase()
            val walkId = trackingUseCases.addWalkUseCase(uiTrackingState.value)
            uiTrackingState.value.pathPoints.forEach { pathPoint ->
                trackingUseCases.addPathPointUseCase(walkId, pathPoint)
            }
            walkHandler.clearWalk()
        }
    }
}
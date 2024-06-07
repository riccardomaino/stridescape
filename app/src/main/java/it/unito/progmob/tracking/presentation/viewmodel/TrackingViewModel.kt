package it.unito.progmob.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.tracking.domain.model.Walk
import it.unito.progmob.tracking.domain.service.WalkHandler
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import it.unito.progmob.tracking.presentation.TrackingEvent
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingUseCases: TrackingUseCases,
    private val walkHandler: WalkHandler
) : ViewModel() {
    // The StateFlow of the WalkState obtained through the WalkHandler
    private val walk: StateFlow<Walk> = walkHandler.walk

    // The tracking feature UI MutableStateFlow which is exposed as a StateFlow to the UI
    private val _uiTrackingState: MutableStateFlow<UiTrackingState> =
        MutableStateFlow(UiTrackingState())
    val uiTrackingState = _uiTrackingState.asStateFlow()

    // The StateFlow used to track if the location provider is enabled
//    private val _isLocationEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
//    val isLocationEnabled = _isLocationEnabled.asStateFlow()

    private val _showStopWalkDialog = MutableStateFlow(false)
    val showStopWalkDialog = _showStopWalkDialog.asStateFlow()

    // The user weight used to calculate the calories burnt
    private var userWeight: Float = 0.0f

    // The user height used to calculate the calories burnt
    private var userHeight: Int = 0

    // The last known location of the user used to open the map and to update the camera position when the user click on location button
    private val _lastKnownLocation = MutableStateFlow<LatLng?>(null)
    val lastKnownLocation = _lastKnownLocation.asStateFlow()

    // The counter of the last known location updates
    private val _lastKnownLocationUpdatesCounter = MutableStateFlow<Long>(0)
    val lastKnownLocationUpdatesCounter = _lastKnownLocationUpdatesCounter.asStateFlow()

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

        walk.onEach { walkState ->
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

        trackSingleLocation()
    }

    fun onEvent(event: TrackingEvent) {
        when (event) {
            is TrackingEvent.StartTrackingService -> startTrackingService()
            is TrackingEvent.ResumeTrackingService -> resumeTrackingService()
            is TrackingEvent.PauseTrackingService -> pauseTrackingService()
            is TrackingEvent.StopTrackingService -> stopTrackingService()
            is TrackingEvent.TrackSingleLocation -> trackSingleLocation()
            is TrackingEvent.ShowStopWalkDialog -> showStopWalkDialog(event.showDialog)
        }
    }

    private fun showStopWalkDialog(showDialog: Boolean) {
        viewModelScope.launch {
            _showStopWalkDialog.update { showDialog }
        }
    }

    private fun trackSingleLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_uiTrackingState.value.isTracking) {
                _lastKnownLocation.update {
                    _uiTrackingState.value.pathPoints.lastLocationPoint()
                        ?.let { lastLocationPoint ->
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

    private fun startTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.startTrackingUseCase()
        }
    }

    private fun resumeTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.resumeTrackingUseCase()
        }
    }

    private fun pauseTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.pauseTrackingUseCase()
        }
    }

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

//    companion object {
//        private val TAG = TrackingViewModel::class.java.simpleName
//    }

}
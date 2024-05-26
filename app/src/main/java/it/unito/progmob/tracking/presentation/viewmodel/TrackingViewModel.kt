package it.unito.progmob.tracking.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.tracking.domain.model.Walk
import it.unito.progmob.tracking.domain.service.WalkHandler
import it.unito.progmob.core.domain.util.WalkUtils
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
): ViewModel() {
    // The StateFlow of the WalkState obtained through the WalkStateHandler
    private val _walk: StateFlow<Walk> = walkHandler.walk

    // The tracking feature UI MutableStateFlow which is exposed as a StateFlow to the UI
    private val _uiTrackingState: MutableStateFlow<UiTrackingState> = MutableStateFlow(UiTrackingState())
    val uiTrackingState: StateFlow<UiTrackingState> = _uiTrackingState.asStateFlow()

    // The user weight used to calculate the calories burnt
    private var userWeight: Float = 0.0f

    // The user height used to calculate the calories burnt
    private var userHeight: Int = 0


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

        _walk.onEach { walkState ->
            _uiTrackingState.update {
                it.copy(
                    isTracking = walkState.isTracking,
                    distanceInMeters = walkState.distanceInMeters,
                    timeInMillis = walkState.timeInMillis,
                    speedInKMH = walkState.speedInKMH,
                    pathPoints = walkState.pathPoints,
                    steps = walkState.steps,
                    caloriesBurnt = WalkUtils.getCaloriesBurnt(userWeight, walkState.distanceInMeters)
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: TrackingEvent){
        when(event){
            is TrackingEvent.StartTrackingService -> startTrackingService()
            is TrackingEvent.ResumeTrackingService-> resumeTrackingService()
            is TrackingEvent.PauseTrackingService -> pauseTrackingService()
            is TrackingEvent.StopTrackingService -> stopTrackingService()
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
        val savedState = _uiTrackingState.value.copy()
        Log.d(TAG, "SAVED STATE: $savedState")

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                Log.d(TAG, "Saving to the database ...")
            }
            launch {
                trackingUseCases.stopTrackingUseCase()
            }
        }
    }

    companion object{
        private val TAG = TrackingViewModel::class.java.simpleName
    }

}
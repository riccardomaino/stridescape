package it.unito.progmob.tracking.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.sensor.MeasurableSensor
import it.unito.progmob.core.domain.state.WalkStateHandler
import it.unito.progmob.core.domain.state.WalkState
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import it.unito.progmob.tracking.presentation.TrackingEvent
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.pow
import kotlin.math.sqrt

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingUseCases: TrackingUseCases,
    @Named("accelerometer") private val accelerometerSensor: MeasurableSensor,
    private val stepCounterSensor: MeasurableSensor,
    private val walkStateHandler: WalkStateHandler
): ViewModel() {
    // The StateFlow of the WalkState obtained through the WalkStateHandler
    private val _walkState: StateFlow<WalkState> = walkStateHandler.walkStateFlow

    // The tracking feature UI MutableStateFlow which is exposed as a StateFlow to the UI
    private val _uiTrackingState: MutableStateFlow<UiTrackingState> = MutableStateFlow(UiTrackingState())
    val uiTrackingState: StateFlow<UiTrackingState> = combine(_walkState, _uiTrackingState) { walkState, _ ->
        Log.d(TAG, "UI UPDATE: $walkState")
        UiTrackingState(
            isTracking = walkState.isTracking,
            distanceInMeters = walkState.distanceInMeters,
            speedInKMH = walkState.speedInKMH,
            pathPoints = walkState.pathPoints,
            steps = walkState.steps,
            caloriesBurnt = WalkUtils.getCaloriesBurnt(userWeight, walkState.distanceInMeters)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiTrackingState())

    // The user weight and height are used to calculate the calories burnt
    private var userWeight: Float = 0.0f
    private var userHeight: Int = 0


    var magnitudePrevious = 0.0
    var steps = 0



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

        viewModelScope.launch(Dispatchers.IO) {
            logging()
        }


        accelerometerSensor.startListening()
        stepCounterSensor.startListening()

        stepCounterSensor.setOnSensorValueChangedListener { values ->
            val numSteps = values[0].toInt()
//            _steps.value = numSteps
            Log.d(TAG, "Step Counter OLD: $numSteps")
        }

        accelerometerSensor.setOnSensorValueChangedListener { values ->

            val x: Float = values[0]
            val y: Float = values[1]
            val z: Float = values[2]

            // WITHOUT GRAVITY
            val magnitude = sqrt(x.pow(2.0f) + y.pow(2.0f) + z.pow(2.0f))


            // WITH EARTH'S GRAVITY INCLUDED
//            val magnitude = (sqrt(x.pow(2) + y.pow(2) + z.pow(2))) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            val magnitudeDelta = magnitude - magnitudePrevious
            magnitudePrevious = magnitude.toDouble()

            if (magnitudeDelta > 6) { // adjust the threshold based on your device
//                _steps.value += 1
                steps++
                Log.d(TAG, "Step Counter NEW: $steps")
            }
        }
    }

    fun onEvent(event: TrackingEvent){
        when(event){
            is TrackingEvent.StartTrackingService -> startTrackingService()
            is TrackingEvent.ResumeTrackingService-> resumeTrackingService()
            is TrackingEvent.PauseTrackingService -> pauseTrackingService()
            is TrackingEvent.StopTrackingService -> stopTrackingService()
        }
    }

    private suspend fun logging(){
        while(true) {
            delay(5000)
            Log.d(TAG, "UI REFERENCE TO WALKSTATE: ${_walkState.value}")
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
        }
    }

    companion object{
        private val TAG = TrackingViewModel::class.java.simpleName
    }

}
package it.unito.progmob.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.state.CaloriesState
import it.unito.progmob.core.domain.state.StepsState
import it.unito.progmob.core.domain.state.WalkState
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import it.unito.progmob.tracking.presentation.TrackingEvent
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingUseCases: TrackingUseCases
): ViewModel() {
    // State of the walk as a MutableStateFlow which is NOT exposed to the UI
    private val _walkState = MutableStateFlow(WalkState())

    // State of the calories as a MutableStateFlow which is NOT exposed to the UI
    private val _caloriesState = MutableStateFlow(CaloriesState())

    // State of the steps as a MutableStateFlow which is NOT exposed to the UI
    private val _stepsState = MutableStateFlow(StepsState())

    // Total state is a combination of the previous three states which is exposed as a StateFlow to the UI
    private val _uiTrackingState = MutableStateFlow(UiTrackingState())
    val uiTrackingState = combine(_walkState, _stepsState, _caloriesState, _uiTrackingState) { walkState, stepsState, caloriesState, uiTrackingState ->
        uiTrackingState.copy(
            isTracking = true,
            distanceInMeters = walkState.distanceInMeters,
            speedInKMH = walkState.speedInKMH,
            pathPoints = walkState.pathPoints,
            steps = stepsState.steps,
            caloriesBurnt = caloriesState.caloriesBurnt
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiTrackingState())

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
            trackingUseCases
        }
    }

    private fun pauseTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    private fun stopTrackingService() {
        viewModelScope.launch(Dispatchers.IO) {
            trackingUseCases.stopTrackingUseCase()
        }
    }

}
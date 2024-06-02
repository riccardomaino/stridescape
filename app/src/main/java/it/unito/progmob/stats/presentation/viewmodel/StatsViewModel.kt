package it.unito.progmob.stats.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.domain.usecase.StatsUseCases
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.stats.presentation.state.UiStatsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsUseCases: StatsUseCases
) : ViewModel() {

    private val _uiStatsState = MutableStateFlow(UiStatsState())
    val uiStatsState = _uiStatsState.asStateFlow()

    var isLoading = mutableStateOf(false)
        private set
    var isDataUpdated = mutableStateOf(false)
        private set


    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStats(
                _uiStatsState.value.startDate,
                _uiStatsState.value.endDate,
                _uiStatsState.value.statsSelected
            )
        }
    }


    /**
     * Handles Stats events emitted from the UI.
     * @param event The StatsEvent to be processed.
     */
    fun onEvent(event: StatsEvent) {
        when (event) {
            is StatsEvent.StatsTypeSelected -> statsTypeSelected(event.statsSelected)
            is StatsEvent.DateRangeSelected -> dateRangeSelected(event.startDate, event.endDate)
        }
    }


    private fun statsTypeSelected(statsSelected: StatsType){
        viewModelScope.launch(Dispatchers.IO) {
            fetchStats(
                _uiStatsState.value.startDate,
                _uiStatsState.value.endDate,
                statsSelected
            )
        }
    }

    private fun dateRangeSelected(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO){
            fetchStats(
                startDate,
                endDate,
                _uiStatsState.value.statsSelected
            )
        }
    }

    private fun fetchStats(startDate: Long, endDate: Long, statsSelected: StatsType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (statsSelected) {
                StatsType.DISTANCE -> {
                    val distanceStatsList =  statsUseCases.getDistanceStatsUseCase(
                        startDate,
                        endDate
                    )
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            distanceChartValues = distanceStatsList
                        )
                    }
                }
                StatsType.TIME -> Log.d("StatsViewModel", "TIME")
                StatsType.CALORIES -> Log.d("StatsViewModel", "CALORIES")
                StatsType.STEPS -> Log.d("StatsViewModel", "STEPS")
                StatsType.SPEED -> Log.d("StatsViewModel", "SPEED")
            }
        }
    }
}
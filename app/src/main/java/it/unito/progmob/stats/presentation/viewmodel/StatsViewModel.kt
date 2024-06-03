package it.unito.progmob.stats.presentation.viewmodel

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
    // MutableStateFlow of UiStatsState used to maintain the current state of the UI.
    private val _uiStatsState = MutableStateFlow(UiStatsState())
    val uiStatsState = _uiStatsState.asStateFlow()

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

    /**
     * Updates the UI state based on the selected stats type after fetching stats data.
     *
     * @param statsSelected The selected stats type.
     */
    private fun statsTypeSelected(statsSelected: StatsType){
        viewModelScope.launch(Dispatchers.IO) {
            fetchStats(
                _uiStatsState.value.startDate,
                _uiStatsState.value.endDate,
                statsSelected
            )
        }
    }

    /**
     * Updates the UI state based on the selected date range after fetching stats data.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     */
    private fun dateRangeSelected(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO){
            fetchStats(
                startDate,
                endDate,
                _uiStatsState.value.statsSelected
            )
        }
    }

    /**
     * Fetches stats data based on the selected stats type and date range.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     * @param statsSelected The selected stats type.
     */
    private fun fetchStats(startDate: Long, endDate: Long, statsSelected: StatsType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (statsSelected) {
                StatsType.DISTANCE -> {
                    val distanceStatsList =  statsUseCases.getDistanceDataUseCase(startDate, endDate)
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            distanceChartValues = distanceStatsList
                        )
                    }
                }
                StatsType.TIME -> {
                    val timeStatsList = statsUseCases.getTimeDataUseCase(startDate, endDate)
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            timeChartValues = timeStatsList
                        )
                    }
                }
                StatsType.CALORIES -> {
                    val caloriesStatsList = statsUseCases.getCaloriesDataUseCase(startDate, endDate)
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            caloriesChartValues = caloriesStatsList
                        )
                    }
                }
                StatsType.STEPS -> {
                    val stepsStatsList = statsUseCases.getStepsDataUseCase(startDate, endDate)
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            stepsChartValues = stepsStatsList
                        )
                    }
                }
                StatsType.SPEED -> {
                    val speedStatsList = statsUseCases.getSpeedDataUseCase(startDate, endDate)
                    _uiStatsState.update {
                        it.copy(
                            startDate = startDate,
                            endDate = endDate,
                            statsSelected = statsSelected,
                            speedChartValues = speedStatsList
                        )
                    }
                }
            }
        }
    }
}
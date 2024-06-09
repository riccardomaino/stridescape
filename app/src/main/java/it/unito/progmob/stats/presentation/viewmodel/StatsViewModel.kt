package it.unito.progmob.stats.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.stats.domain.model.RangeType
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

/**
 * ViewModel class that handles the stats feature.
 *
 * @param statsUseCases The use cases related to the stats feature.
 */
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsUseCases: StatsUseCases
) : ViewModel() {
    // MutableStateFlow of UiStatsState used to maintain the current state of the UI.
    private val _uiStatsState = MutableStateFlow(UiStatsState())
    val uiStatsState = _uiStatsState.asStateFlow()

    // MutableStateFlow of Boolean used to determine if the stats data has been fetched.
    private val _uiStatsFetched  = MutableStateFlow(false)
    val uiStatsFetched = _uiStatsFetched.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchStats(
                _uiStatsState.value.statsSelected,
                _uiStatsState.value.rangeSelected
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
            is StatsEvent.RangeTypeSelected -> rangeTypeSelected(event.rangeSelected)
        }
    }

    /**
     * Updates the UI state based on the selected stats type and fetches stats data.
     *
     * @param statsSelected The selected stats type.
     */
    private fun statsTypeSelected(statsSelected: StatsType){
        viewModelScope.launch(Dispatchers.IO) {
            _uiStatsFetched.update { false }
            fetchStats(
                statsSelected,
                _uiStatsState.value.rangeSelected
            )
        }
    }

    /**
     * Updates the UI state based on the selected range type and fetches stats data.
     *
     * @param rangeSelected The type of date range selected.
     */
    private fun rangeTypeSelected(rangeSelected: RangeType) {
        viewModelScope.launch(Dispatchers.IO){
            _uiStatsFetched.update { false }
            fetchStats(
                _uiStatsState.value.statsSelected,
                rangeSelected
            )
        }
    }

    /**
     * Fetches stats data based on the selected stats type and date range.
     *
     * @param statsSelected The selected stats type.
     */
    private fun fetchStats(statsSelected: StatsType, rangeSelected: RangeType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (statsSelected) {
                StatsType.DISTANCE -> {
                    val distanceStatsList =  if(rangeSelected != RangeType.YEAR) {
                        statsUseCases.getWeekOrMonthDistanceStatUseCase(rangeSelected)
                    } else {
                        statsUseCases.getYearDistanceStatUseCase()
                    }
                    _uiStatsState.update {
                        it.copy(
                            statsSelected = statsSelected,
                            rangeSelected = rangeSelected,
                            distanceChartValues = distanceStatsList
                        )
                    }
                    _uiStatsFetched.update { true }
                }
                StatsType.TIME -> {
                    val timeStatsList = if(rangeSelected != RangeType.YEAR) {
                        statsUseCases.getWeekOrMonthTimeStatUseCase(rangeSelected)
                    } else {
                        statsUseCases.getYearTimeStatUseCase()
                    }
                    _uiStatsState.update {
                        it.copy(
                            statsSelected = statsSelected,
                            rangeSelected = rangeSelected,
                            timeChartValues = timeStatsList
                        )
                    }
                    _uiStatsFetched.update { true }
                }
                StatsType.CALORIES -> {
                    val caloriesStatsList = if(rangeSelected != RangeType.YEAR) {
                        statsUseCases.getWeekOrMonthCaloriesStatUseCase(rangeSelected)
                    } else {
                        statsUseCases.getYearCaloriesStatUseCase()
                    }
                    _uiStatsState.update {
                        it.copy(
                            statsSelected = statsSelected,
                            rangeSelected = rangeSelected,
                            caloriesChartValues = caloriesStatsList
                        )
                    }
                    _uiStatsFetched.update { true }
                }
                StatsType.STEPS -> {
                    val stepsStatsList = if(rangeSelected != RangeType.YEAR) {
                        statsUseCases.getWeekOrMonthStepsStatUseCase(rangeSelected)
                    } else {
                        statsUseCases.getYearStepsStatUseCase()
                    }
                    _uiStatsState.update {
                        it.copy(
                            statsSelected = statsSelected,
                            rangeSelected = rangeSelected,
                            stepsChartValues = stepsStatsList
                        )
                    }
                    _uiStatsFetched.update { true }
                }
                StatsType.SPEED -> {
                    val speedStatsList = if(rangeSelected != RangeType.YEAR) {
                        statsUseCases.getWeekOrMonthSpeedStatUseCase(rangeSelected)
                    } else {
                        statsUseCases.getYearSpeedStatUseCase()
                    }
                    _uiStatsState.update {
                        it.copy(
                            statsSelected = statsSelected,
                            rangeSelected = rangeSelected,
                            speedChartValues = speedStatsList
                        )
                    }
                    _uiStatsFetched.update { true }
                }
            }
        }
    }
}
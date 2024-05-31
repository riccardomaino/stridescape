package it.unito.progmob.stats.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.domain.usecase.StatsUseCases
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.stats.presentation.state.UiStatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsUseCases: StatsUseCases
) : ViewModel() {

    private val _uiStatsState = MutableStateFlow(UiStatsState.DEFAULT)
    val uiStatsState = _uiStatsState.asStateFlow()

    /**
     * Handles Stats events emitted from the UI.
     * @param event The StatsEvent to be processed.
     */
    fun onEvent(event: StatsEvent) {
        when (event) {
            is StatsEvent.FilterSelected -> onFilterSelected(event.statsSelected)
        }
    }

    private fun onFilterSelected(statsSelected: StatsType){
        viewModelScope.launch {
            _uiStatsState.update {
                it.copy(
                    statsSelected = statsSelected
                )
            }
        }
    }
}
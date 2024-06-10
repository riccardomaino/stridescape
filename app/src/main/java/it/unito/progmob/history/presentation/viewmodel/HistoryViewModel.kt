package it.unito.progmob.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.usecase.HistoryUseCases
import it.unito.progmob.history.presentation.HistoryEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCases: HistoryUseCases
) : ViewModel() {

    private val _allGroupedWalks = MutableStateFlow(emptyList<AllWalksPerDate>())
    val allGroupedWalks = _allGroupedWalks.asStateFlow()

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded = _isDataLoaded.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchWalks(
                startDate = DateUtils.getInstantOfDateFromNow(7, DateUtils.DateOperation.MINUS).toEpochMilliseconds(),
                endDate = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds()
            )
        }
    }

    /**
     * Handles History events emitted from the UI.
     * @param event The HistoryEvent to be processed.
     */
    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.DateRangeSelected -> dateRangeSelected(event.startDate, event.endDate)
        }
    }

    /**
     * Updates the UI state based on the selected date range for fetching data.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     */
    private fun dateRangeSelected(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO){
            fetchWalks(
                startDate = startDate,
                endDate = endDate,
            )
        }
    }

    /**
     * Fetches the walks data from the database based on the selected date range.
     *
     * @param startDate The start date in milliseconds.
     * @param endDate The end date in milliseconds.
     */
    private fun fetchWalks(startDate: Long, endDate: Long){
        viewModelScope.launch(Dispatchers.IO) {
            _isDataLoaded.update { false }
            val resultAllWalksPerDate = historyUseCases.getWalksWithPathPointsUseCase(
                startDate = startDate,
                endDate = endDate
            )
            _allGroupedWalks.update { resultAllWalksPerDate }
            _isDataLoaded.update { true }
        }
    }
}
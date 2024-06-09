package it.unito.progmob.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.usecase.HistoryUseCases
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
    var allWalks: MutableList<AllWalksPerDate> = mutableListOf()
        private set

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded = _isDataLoaded.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isDataLoaded.update { false }
            allWalks = historyUseCases.getWalksWithPathPointsUseCase()
            _isDataLoaded.update { true }
        }
    }
}
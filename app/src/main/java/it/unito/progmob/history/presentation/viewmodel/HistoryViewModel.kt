package it.unito.progmob.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.usecase.HistoryUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCases: HistoryUseCases
) : ViewModel() {
    var allWalks: MutableList<AllWalksPerDate> = mutableListOf()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allWalks = historyUseCases.getWalksWithPathPointsUseCase()
        }
    }
}
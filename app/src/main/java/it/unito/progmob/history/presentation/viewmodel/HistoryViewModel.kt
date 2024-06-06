package it.unito.progmob.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.history.domain.usecase.HistoryUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCases: HistoryUseCases
) : ViewModel() {
//    lateinit var allWalks: MutableList<AllWalksPerDate>
    val allWalks = historyUseCases.getWalksWithPathPointsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf())

//    init {
//        viewModelScope.launch {
//            allWalks = historyUseCases.getWalksWithPathPointsUseCase()
//        }
//    }


}
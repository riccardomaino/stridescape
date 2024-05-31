package it.unito.progmob.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.home.domain.usecase.HomeUseCases
import it.unito.progmob.home.presentation.HomeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _currentDayOfWeek = MutableStateFlow(DateUtils.getCurrentDayOfWeek())
    val currentDayOfWeek = _currentDayOfWeek.asStateFlow()

    private val currentDate = DateUtils.getCurrentDate(pattern = "yyyy/MM/dd")

    val stepsCurrentDay = homeUseCases.getDayStepsUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val caloriesCurrentDay = homeUseCases.getDayCaloriesUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val distanceCurrentDay = homeUseCases.getDayDistanceUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    val timeCurrentDay = homeUseCases.getDayTimeUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0L)

    val stepsTargetCurrentDay = homeUseCases.getDateTargetUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    val weeklySteps = homeUseCases.getWeeklyStepsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), intArrayOf())

    /**
     * Handles Home events emitted from the UI.
     * @param event The HomeEvent to be processed.
     */
    fun onEvent(event: HomeEvent) {

    }
}
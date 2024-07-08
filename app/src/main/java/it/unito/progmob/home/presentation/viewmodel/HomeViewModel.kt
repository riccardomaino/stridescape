package it.unito.progmob.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.home.domain.usecase.HomeUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 *
 * @property homeUseCases Use cases for the Home screen.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    /**
     * The current day of the week.
     */
    private val _currentDayOfWeek = MutableStateFlow(DateUtils.getCurrentDayOfWeek())
    val currentDayOfWeek = _currentDayOfWeek.asStateFlow()

    /**
     * The current date, formatted as a string.
     */
    private val currentDate = DateUtils.formattedCurrentDate()

    /**
     * The number of steps taken on the current day.
     */
    val stepsCurrentDay = homeUseCases.getDayStepsUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    /**
     * The number of calories burned on the current day.
     */
    val caloriesCurrentDay = homeUseCases.getDayCaloriesUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    /**
     * The distance traveled on the current day.
     */
    val distanceCurrentDay = homeUseCases.getDayDistanceUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    /**
     * The time spent active on the current day.
     */
    val timeCurrentDay = homeUseCases.getDayTimeUseCase(currentDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0L)

    /**
     * The target number of steps for the current day.
     */
    val stepsTargetCurrentDay = homeUseCases.getTargetUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    /**
     * The number of steps taken each day of the current week.
     */
    val weeklySteps = homeUseCases.getWeeklyStepsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), intArrayOf(0, 0, 0, 0, 0, 0, 0))

    /**
     * The target number of steps for each day of the current week.
     */
    val weeklyTarget = homeUseCases.getWeeklyTargetUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), intArrayOf(1, 1, 1, 1, 1, 1, 1))
}
package it.unito.progmob.core.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.domain.usecase.MainUseCases
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.presentation.MainEvent
import it.unito.progmob.core.presentation.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCases: MainUseCases,
) : ViewModel() {

    var startDestination by mutableStateOf(Route.OnBoardingNavigationRoute.route)
        private set

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        mainUseCases.readOnboardingEntryUseCase().onEach { shouldStartFromHomeScreen ->
            startDestination = if(shouldStartFromHomeScreen) {
                Route.MainNavigationRoute.route
            } else {
                Route.OnBoardingNavigationRoute.route
            }
            delay(600)
            _isReady.value = true
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            mainUseCases.checkTargetExistUseCase(DateUtils.getCurrentDate(pattern = "yyyy/MM/dd"))
        }
    }

    /**
     * Handles Main events emitted from the UI.
     * @param event The MainEvent to be processed.
     */
    fun onEvent(event: MainEvent) {
    }
}
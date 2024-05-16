package it.unito.progmob.core.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.presentation.MainEvent
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases,
) : ViewModel() {

    // Da utilizzare in caso di splash screen
    //    var splashCondition by mutableStateOf(true)
    //        private set
    var startDestination by mutableStateOf(Route.StartNavigationRoute.route)
        private set

    init {
        onBoardingUseCases.readOnboardingEntryUseCase().onEach { shouldStartFromHomeScreen ->
            startDestination = if(shouldStartFromHomeScreen) {
                Route.HomeNavigationRoute.route
            } else {
                Route.StartNavigationRoute.route
            }

            // Utilizzato con la splash screen
//            delay(300)
//            splashCondition = false
        }.launchIn(viewModelScope)
    }

    /**
     * Handles Main events emitted from the UI.
     * @param event The MainEvent to be processed.
     */
    fun onEvent(event: MainEvent) {
    }
}
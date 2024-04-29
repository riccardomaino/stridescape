package it.unito.progmob.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.core.presentation.navgraph.Route
import it.unito.progmob.onboarding.domain.usecases.ReadOnboardingEntry
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val readOnboardingEntry: ReadOnboardingEntry,
) : ViewModel() {

// Da utilizzare in caso di splash screen
//    var splashCondition by mutableStateOf(true)
//        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        readOnboardingEntry().onEach { shouldStartFromHomeScreen ->
            startDestination = if(shouldStartFromHomeScreen) {
                Route.HomeNavigation.route
            } else {
                Route.AppStartNavigation.route
            }

            // Utilizzato con la splash screen
//            delay(300)
//            splashCondition = false
        }.launchIn(viewModelScope)
    }
}
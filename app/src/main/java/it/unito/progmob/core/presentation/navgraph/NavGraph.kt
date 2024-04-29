package it.unito.progmob.core.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import it.unito.progmob.onboarding.presentation.OnBoardingScreen
import it.unito.progmob.onboarding.presentation.viewmodels.OnBoardingViewModel


@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
                OnBoardingScreen(
                    event = onBoardingViewModel::onEvent
                )
            }
        }
        
        navigation(
            route = Route.HomeNavigation.route,
            startDestination = Route.HomeNavigationScreen.route
        ){
            composable(
                route = Route.HomeNavigationScreen.route
            ) {
                Text(text = "Home Navigator Screen")
            }
        }
    }
}
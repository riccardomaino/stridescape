package it.unito.progmob.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import it.unito.progmob.MainActivity
import it.unito.progmob.home.presentation.HomeScreen
import it.unito.progmob.home.presentation.viewmodel.HomeViewModel
import it.unito.progmob.onboarding.presentation.OnBoardingScreen
import it.unito.progmob.onboarding.presentation.viewmodel.OnBoardingViewModel


@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.StartNavigationRoute.route,
            startDestination = Route.OnBoardingScreenRoute.route
        ) {
            composable(
                route = Route.OnBoardingScreenRoute.route
            ) {
                val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
                OnBoardingScreen(
                    onBoardingEvent = onBoardingViewModel::onEvent,
                    navController = navController
                )
            }
        }
        
        navigation(
            route = Route.HomeNavigationRoute.route,
            startDestination = Route.HomeScreenRoute.route
        ) {
            composable(
                route = Route.HomeScreenRoute.route
            ) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    homeEvent = homeViewModel::onEvent,
                    homeViewModel = homeViewModel,
                    navController = navController
                )
            }
        }
    }
}
package it.unito.progmob.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
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
                val visiblePermissionDialogQueue by homeViewModel.visiblePermissionDialogQueue.collectAsState()
                val permissionsToRequest = homeViewModel.permissionsToRequest
                val steps by homeViewModel.stepsCount.collectAsState()
                val currentDay by homeViewModel.currentDay.collectAsState()

                HomeScreen(
                    homeEvent = homeViewModel::onEvent,
                    navController = navController,
                    visiblePermissionDialogQueue = visiblePermissionDialogQueue,
                    permissionsToRequest = permissionsToRequest,
                    currentDay = currentDay,
                    steps = steps
                )
            }
        }
    }
}
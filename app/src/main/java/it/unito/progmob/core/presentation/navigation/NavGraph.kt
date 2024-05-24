package it.unito.progmob.core.presentation.navigation

import android.util.Log
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
import it.unito.progmob.onboarding.presentation.OnBoardingProfileScreen
import it.unito.progmob.onboarding.presentation.OnBoardingScreen
import it.unito.progmob.onboarding.presentation.viewmodel.OnBoardingViewModel
import it.unito.progmob.tracking.presentation.TrackingScreen
import it.unito.progmob.tracking.presentation.viewmodel.TrackingViewModel


@Composable
fun NavGraph(
    startDestination: String,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.OnBoardingNavigationRoute.route,
            startDestination = Route.OnBoardingScreenRoute.route
        ) {
            composable(
                route = Route.OnBoardingScreenRoute.route
            ) {
                OnBoardingScreen(
                    navController = navController
                )
            }
            composable(
                route = Route.OnBoardingProfileScreenRoute.route
            ) {
                val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()

                OnBoardingProfileScreen(
                    onBoardingEvent = onBoardingViewModel::onEvent,
                    navController = navController,
                    userName = onBoardingViewModel.userName,
                    userHeight = onBoardingViewModel.userHeight,
                    userWeight = onBoardingViewModel.userWeight
                )
            }
        }

        navigation(
            route = Route.MainNavigationRoute.route,
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

                Log.d("NavGraph", "visiblePermissionDialogQueue: $visiblePermissionDialogQueue")

                HomeScreen(
                    homeEvent = homeViewModel::onEvent,
                    navController = navController,
                    visiblePermissionDialogQueue = visiblePermissionDialogQueue,
                    permissionsToRequest = permissionsToRequest,
                    currentDay = currentDay,
                    steps = steps
                )
            }

            composable(
                route = Route.TrackingScreenRoute.route
            ) {
                val trackingViewModel = hiltViewModel<TrackingViewModel>()
                val uiTrackingState by trackingViewModel.uiTrackingState.collectAsState()
                TrackingScreen(
                    trackingEvent = trackingViewModel::onEvent,
                    navController = navController,
                    uiTrackingState = trackingViewModel.uiTrackingState
                )
            }
        }
    }
}
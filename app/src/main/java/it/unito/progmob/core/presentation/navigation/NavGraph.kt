package it.unito.progmob.core.presentation.navigation

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import it.unito.progmob.core.domain.Constants.TRACKING_DEEP_LINK
import it.unito.progmob.history.presentation.HistoryScreen
import it.unito.progmob.history.presentation.viewmodel.HistoryViewModel
import it.unito.progmob.home.presentation.HomeScreen
import it.unito.progmob.home.presentation.viewmodel.HomeViewModel
import it.unito.progmob.onboarding.presentation.OnBoardingProfileScreen
import it.unito.progmob.onboarding.presentation.OnBoardingScreen
import it.unito.progmob.onboarding.presentation.viewmodel.OnBoardingViewModel
import it.unito.progmob.profile.presentation.ProfileScreen
import it.unito.progmob.profile.presentation.viewmodel.ProfileViewModel
import it.unito.progmob.stats.presentation.StatsScreen
import it.unito.progmob.stats.presentation.viewmodel.StatsViewModel
import it.unito.progmob.tracking.presentation.TrackingScreen
import it.unito.progmob.tracking.presentation.viewmodel.TrackingViewModel

/**
 * Defines the navigation graph for the application.
 *
 * @param startDestination The initial destination of the navigation graph.
 * @param navController The [NavHostController] used to manage navigation.
 */
@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        // Onboarding navigation graph
        navigation(
            route = Route.OnBoardingNavigationRoute.route,
            startDestination = Route.OnBoardingScreenRoute.route,
            exitTransition = { fadeOut(animationSpec = tween(200, delayMillis = 0)) }
        ) {
            // Onboarding welcome screen
            composable(
                route = Route.OnBoardingScreenRoute.route,
            ) {
                OnBoardingScreen(
                    navController = navController
                )
            }
            // Onboarding profile screen
            composable(
                route = Route.OnBoardingProfileScreenRoute.route,
                enterTransition = { fadeIn(animationSpec = tween(200, delayMillis = 0)) },
                exitTransition = { fadeOut(animationSpec = tween(200, delayMillis = 0)) }
            ) {
                val onBoardingViewModel = hiltViewModel<OnBoardingViewModel>()
                val onBoardingState by onBoardingViewModel.onBoardingState
                OnBoardingProfileScreen(
                    onBoardingEvent = onBoardingViewModel::onEvent,
                    navController = navController,
                    onBoardingState = onBoardingState
                )
            }
        }

        // Main navigation graph
        navigation(
            route = Route.MainNavigationRoute.route,
            startDestination = Route.HomeScreenRoute.route
        ) {
            // Home screen
            composable(
                route = Route.HomeScreenRoute.route
            ) {
                val homeViewModel = hiltViewModel<HomeViewModel>()

                // Collect state variables for the Home screen from the home screen viewmodel
                val currentDayOfWeek by homeViewModel.currentDayOfWeek.collectAsState()
                val stepsCurrentDay by homeViewModel.stepsCurrentDay.collectAsState()
                val caloriesCurrentDay by homeViewModel.caloriesCurrentDay.collectAsState()
                val distanceCurrentDay by homeViewModel.distanceCurrentDay.collectAsState()
                val timeCurrentDay by homeViewModel.timeCurrentDay.collectAsState()
                val stepsTargetCurrentDay by homeViewModel.stepsTargetCurrentDay.collectAsState()
                val weeklySteps by homeViewModel.weeklySteps.collectAsState()
                val weeklyTarget by homeViewModel.weeklyTarget.collectAsState()

                HomeScreen(
                    currentDayOfWeek = currentDayOfWeek,
                    stepsCurrentDay = stepsCurrentDay,
                    caloriesCurrentDay = caloriesCurrentDay,
                    distanceCurrentDay = distanceCurrentDay,
                    timeCurrentDay = timeCurrentDay,
                    stepsTargetCurrentDay = stepsTargetCurrentDay,
                    weeklySteps = weeklySteps,
                    weeklyTarget = weeklyTarget,
                )
            }

            // Tracking screen
            composable(
                route = Route.TrackingScreenRoute.route,
                deepLinks = listOf(navDeepLink {
                    uriPattern = TRACKING_DEEP_LINK
                    action = Intent.ACTION_VIEW
                }
                ),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                        animationSpec = tween(250, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                        animationSpec = tween(250, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                },
            ) {
                val trackingViewModel = hiltViewModel<TrackingViewModel>()

                // Collect state variables for the Tracking screen from the tracking screen viewmodel
                val uiTrackingState by trackingViewModel.uiTrackingState.collectAsState()
                val lastKnownLocation by trackingViewModel.lastKnownLocation.collectAsState()
                val lastKnownLocationUpdatesCounter by trackingViewModel.lastKnownLocationUpdatesCounter.collectAsState()
                val showStopWalkDialog by trackingViewModel.showStopWalkDialog.collectAsState()
                val isLocationEnabled by trackingViewModel.isLocationEnabled.collectAsState()

                TrackingScreen(
                    trackingEvent = trackingViewModel::onEvent,
                    navController = navController,
                    uiTrackingState = uiTrackingState,
                    lastKnownLocation = lastKnownLocation,
                    lastKnownLocationUpdatesCounter = lastKnownLocationUpdatesCounter,
                    showStopWalkDialog = showStopWalkDialog,
                    isLocationEnabled = isLocationEnabled,
                )
            }

            // Stats screen
            composable(
                route = Route.StatsScreenRoute.route
            ) {
                val statsViewModel = hiltViewModel<StatsViewModel>()

                // Collect state variables for the Stats screen from the stats screen viewmodel
                val uiStatsState by statsViewModel.uiStatsState.collectAsState()
                val isDataLoaded by statsViewModel.isDataLoaded.collectAsState()

                StatsScreen(
                    statsEvent = statsViewModel::onEvent,
                    uiStatsState = uiStatsState,
                    isDataLoaded = isDataLoaded,
                )

            }

            // History screen
            composable(
                route = Route.HistoryScreenRoute.route
            ) {
                val historyViewModel = hiltViewModel<HistoryViewModel>()

                // Collect state variables for the History screen from the history screen viewmodel
                val allGroupedWalks by historyViewModel.allGroupedWalks.collectAsState()
                val isDataLoaded by historyViewModel.isDataLoaded.collectAsState()

                HistoryScreen(
                    historyEvent = historyViewModel::onEvent,
                    allGroupedWalks = allGroupedWalks,
                    isDataLoaded = isDataLoaded
                )
            }

            // Profile screen
            composable(
                route = Route.ProfileScreenRoute.route,
                enterTransition = { fadeIn(animationSpec = tween(150, delayMillis = 0)) },
                exitTransition = { fadeOut(animationSpec = tween(150, delayMillis = 0)) }
            ) {
                val profileViewModel = hiltViewModel<ProfileViewModel>()
                val profileState by profileViewModel.profileState
                ProfileScreen(
                    profileEvent = profileViewModel::onEvent,
                    profileState = profileState,
                )
            }
        }
    }
}
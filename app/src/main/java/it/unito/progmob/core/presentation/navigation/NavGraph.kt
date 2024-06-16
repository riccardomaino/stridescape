package it.unito.progmob.core.presentation.navigation

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import it.unito.progmob.main.presentation.MainEvent
import it.unito.progmob.onboarding.presentation.OnBoardingProfileScreen
import it.unito.progmob.onboarding.presentation.OnBoardingScreen
import it.unito.progmob.onboarding.presentation.viewmodel.OnBoardingViewModel
import it.unito.progmob.profile.presentation.ProfileScreen
import it.unito.progmob.profile.presentation.viewmodel.ProfileViewModel
import it.unito.progmob.stats.presentation.StatsScreen
import it.unito.progmob.stats.presentation.viewmodel.StatsViewModel
import it.unito.progmob.tracking.presentation.TrackingScreen
import it.unito.progmob.tracking.presentation.viewmodel.TrackingViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

/**
 * Defines the navigation graph for the application.
 *
 * @param startDestination The initial destination of the navigation graph.
 * @param navController The [NavHostController] used to manage navigation.
 */
@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController,
    mainEvent: (MainEvent) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        // OnBoarding Navigation Graph
        navigation(
            route = Route.OnBoardingNavigationRoute.route,
            startDestination = Route.OnBoardingScreenRoute.route,
            exitTransition = { fadeOut(animationSpec = tween(200, delayMillis = 0)) }
        ) {
            // OnBoarding Welcome Screen
            composable(
                route = Route.OnBoardingScreenRoute.route,
            ) {
                OnBoardingScreen(
                    navController = navController
                )
            }
            // OnBoarding Profile Screen
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

        // Main Navigation Graph
        navigation(
            route = Route.MainNavigationRoute.route,
            startDestination = Route.HomeScreenRoute.route
        ) {
            // Home Screen
            composable(
                route = Route.HomeScreenRoute.route
            ) {
                // Handle the back press to show the floating action button only on the Home screen
                BackHandler(enabled = !backPressHandled) {
                    when(navController.previousBackStackEntry?.destination?.route) {
                        Route.HomeScreenRoute.route -> mainEvent(MainEvent.ShowFloatingActionButton(true))
                        else -> mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                        onBackPressedDispatcher?.onBackPressed()
                        backPressHandled = false
                    }
                }

                // Get the ViewModel for the Home screen and collect the related state variables
                val homeViewModel = hiltViewModel<HomeViewModel>()
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

            // Tracking Screen
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
                // Handle the back press to show the floating action button only on the Home screen
                BackHandler(enabled = !backPressHandled) {
                    when(navController.previousBackStackEntry?.destination?.route) {
                        Route.HomeScreenRoute.route -> mainEvent(MainEvent.ShowFloatingActionButton(true))
                        else -> mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                        onBackPressedDispatcher?.onBackPressed()
                        backPressHandled = false
                    }
                }

                // Get the ViewModel for the Tracking screen and collect the related state variables
                val trackingViewModel = hiltViewModel<TrackingViewModel>()
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

            // Stats Screen
            composable(
                route = Route.StatsScreenRoute.route
            ) {
                // Handle the back press to show the floating action button only on the Home screen
                BackHandler(enabled = !backPressHandled) {
                    when(navController.previousBackStackEntry?.destination?.route) {
                        Route.HomeScreenRoute.route -> mainEvent(MainEvent.ShowFloatingActionButton(true))
                        else -> mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                        onBackPressedDispatcher?.onBackPressed()
                        backPressHandled = false
                    }
                }

                // Get the ViewModel for the Stats screen and collect the related state variables
                val statsViewModel = hiltViewModel<StatsViewModel>()
                val uiStatsState by statsViewModel.uiStatsState.collectAsState()
                val isDataLoaded by statsViewModel.isDataLoaded.collectAsState()

                StatsScreen(
                    statsEvent = statsViewModel::onEvent,
                    uiStatsState = uiStatsState,
                    isDataLoaded = isDataLoaded,
                )

            }

            // History Screen
            composable(
                route = Route.HistoryScreenRoute.route
            ) {
                // Handle the back press to show the floating action button only on the Home screen
                BackHandler(enabled = !backPressHandled) {
                    when(navController.previousBackStackEntry?.destination?.route) {
                        Route.HomeScreenRoute.route -> mainEvent(MainEvent.ShowFloatingActionButton(true))
                        else -> mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                        onBackPressedDispatcher?.onBackPressed()
                        backPressHandled = false
                    }
                }

                // Get the ViewModel for the History screen and collect the related state variables
                val historyViewModel = hiltViewModel<HistoryViewModel>()
                val allGroupedWalks by historyViewModel.allGroupedWalks.collectAsState()
                val isDataLoaded by historyViewModel.isDataLoaded.collectAsState()

                HistoryScreen(
                    historyEvent = historyViewModel::onEvent,
                    allGroupedWalks = allGroupedWalks,
                    isDataLoaded = isDataLoaded
                )
            }

            // Profile Screen
            composable(
                route = Route.ProfileScreenRoute.route,
            ) {
                // Handle the back press to show the floating action button only on the Home screen
                BackHandler(enabled = !backPressHandled) {
                    when(navController.previousBackStackEntry?.destination?.route) {
                        Route.HomeScreenRoute.route -> mainEvent(MainEvent.ShowFloatingActionButton(true))
                        else -> mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                    backPressHandled = true
                    coroutineScope.launch {
                        awaitFrame()
                        onBackPressedDispatcher?.onBackPressed()
                        backPressHandled = false
                    }
                }
                // Get the ViewModel for the Profile screen and collect the related state variables
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
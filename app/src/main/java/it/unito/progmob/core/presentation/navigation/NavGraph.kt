package it.unito.progmob.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
                route = Route.OnBoardingScreenRoute.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                }
            ) {
                OnBoardingScreen(
                    navController = navController
                )
            }
            composable(
                route = Route.OnBoardingProfileScreenRoute.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(220, delayMillis = 90))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                }
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
                route = Route.HomeScreenRoute.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                }
            ) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val visiblePermissionDialogQueue by homeViewModel.visiblePermissionDialogQueue.collectAsState()
                val currentDayOfWeek by homeViewModel.currentDayOfWeek.collectAsState()
                val stepsCurrentDay by homeViewModel.stepsCurrentDay.collectAsState()
                val caloriesCurrentDay by homeViewModel.caloriesCurrentDay.collectAsState()
                val distanceCurrentDay by homeViewModel.distanceCurrentDay.collectAsState()
                val timeCurrentDay by homeViewModel.timeCurrentDay.collectAsState()
                val stepsTargetCurrentDay by homeViewModel.stepsTargetCurrentDay.collectAsState()

                HomeScreen(
                    homeEvent = homeViewModel::onEvent,
                    navController = navController,
                    visiblePermissionDialogQueue = visiblePermissionDialogQueue,
                    currentDayOfWeek = currentDayOfWeek,
                    stepsCurrentDay = stepsCurrentDay,
                    caloriesCurrentDay = caloriesCurrentDay,
                    distanceCurrentDay = distanceCurrentDay,
                    timeCurrentDay = timeCurrentDay,
                    stepsTargetCurrentDay = stepsTargetCurrentDay
                )
            }

            composable(
                route = Route.TrackingScreenRoute.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = 90))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200, delayMillis = 90)
                    ) + fadeOut(animationSpec = tween(200, delayMillis = 90))
                }
            ) {
                val trackingViewModel = hiltViewModel<TrackingViewModel>()
                val uiTrackingState by trackingViewModel.uiTrackingState.collectAsState()
                TrackingScreen(
                    trackingEvent = trackingViewModel::onEvent,
                    navController = navController,
                    uiTrackingState = uiTrackingState
                )
            }
        }
    }
}
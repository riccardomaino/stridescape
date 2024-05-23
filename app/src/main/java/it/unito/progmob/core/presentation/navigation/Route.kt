package it.unito.progmob.core.presentation.navigation

sealed class Route (
    val route: String
){
    data object OnBoardingNavigationRoute: Route(route = "onBoardingNavigationRoute")
    data object OnBoardingScreenRoute: Route(route = "onBoardingScreenRoute")
    data object OnBoardingProfileScreenRoute: Route(route = "onBoardingProfileScreenRoute")
    data object MainNavigationRoute: Route(route = "mainNavigationRoute")
    data object HomeScreenRoute: Route(route = "homeScreenRoute")
    data object TrackingScreenRoute: Route(route = "trackingScreenRoute")
}
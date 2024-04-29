package it.unito.progmob.core.presentation.navgraph

sealed class Route (
    val route: String
){
    data object OnBoardingScreen: Route(route = "onBoardingScreen")
    data object AppStartNavigation: Route(route = "appStartNavigation")
    data object HomeNavigation: Route(route = "stepsViewNavigation")
    data object HomeNavigationScreen: Route(route = "stepsViewNavigationScreen")
}
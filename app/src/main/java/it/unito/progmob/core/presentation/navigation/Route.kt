package it.unito.progmob.core.presentation.navigation

sealed class Route (
    val route: String
){
    data object StartNavigationRoute: Route(route = "startNavigationRoute")
    data object OnBoardingScreenRoute: Route(route = "onBoardingScreenRoute")
    data object HomeNavigationRoute: Route(route = "stepsViewNavigation")
    data object HomeScreenRoute: Route(route = "stepsViewNavigationScreen")
}
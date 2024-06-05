package it.unito.progmob.core.presentation.navigation

sealed class Route (
    val route: String
){
    data object OnBoardingNavigationRoute: Route(route = "stridescape/onboarding")
    data object OnBoardingScreenRoute: Route(route = "stridescape/onboarding/welcome")
    data object OnBoardingProfileScreenRoute: Route(route = "stridescape/onboarding/profile")
    data object MainNavigationRoute: Route(route = "stridescape/main")
    data object HomeScreenRoute: Route(route = "stridescape/main/home")
    data object TrackingScreenRoute: Route(route = "stridescape/main/tracking")
    data object StatsScreenRoute: Route(route = "stridescape/main/stats")
}
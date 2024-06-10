package it.unito.progmob.core.presentation.navigation

/**
 * A sealed class representing the different routes in the application.
 *
 * @property route The route string.
 */
sealed class Route (
    val route: String
){
    /** Route for the OnBoarding navigation graph. */
    data object OnBoardingNavigationRoute: Route(route = "stridescape/onboarding")

    /** Route for the OnBoarding welcome screen. */
    data object OnBoardingScreenRoute: Route(route = "stridescape/onboarding/welcome")

    /** Route for the OnBoarding profile screen. */
    data object OnBoardingProfileScreenRoute: Route(route = "stridescape/onboarding/profile")

    /** Route for the Main navigation graph. */
    data object MainNavigationRoute: Route(route = "stridescape/main")

    /** Route for the Home screen. */
    data object HomeScreenRoute: Route(route = "stridescape/main/home")

    /** Route for the Tracking screen. */
    data object TrackingScreenRoute: Route(route = "stridescape/main/tracking")

    /** Route for the Stats screen. */
    data object StatsScreenRoute: Route(route = "stridescape/main/stats")

    /** Route for the History screen. */
    data object HistoryScreenRoute: Route(route = "historyScreenRoute")

    /** Route for the Profile screen. */
    data object ProfileScreenRoute: Route(route = "stridescape/main/profile")
}
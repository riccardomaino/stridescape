package it.unito.progmob.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.main.presentation.MainEvent
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.floatingActionButtonSize
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.navigationBarHeight
import it.unito.progmob.ui.theme.navigationBarShadow
import it.unito.progmob.ui.theme.small

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    onClickFloatingActionButton: () -> Unit = {},
    currentBackStackEntry: NavBackStackEntry?,
    navController: NavController,
    isActionButtonShown: Boolean,
    mainEvent: (MainEvent) -> Unit
) {
    val selectedPageColor = MaterialTheme.colorScheme.primary
    val hapticFeedback = LocalView.current
    val animatedFloat = animateFloatAsState(
        targetValue = if (isActionButtonShown) 1f else 0f,
        animationSpec = tween(
            durationMillis = 150,
            delayMillis = 100
        ),
        label = "Floating action button animation"
    )

    val animatedFloatReverse = animateFloatAsState(
        targetValue = if (!isActionButtonShown) 0f else 1f,
        animationSpec = tween(
            durationMillis = 250,
        ),
        label = "Floating action button animation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomAppBar(
            tonalElevation = 0.dp,
            contentPadding = PaddingValues(0.dp),
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .width(LocalContext.current.resources.configuration.screenWidthDp.dp * 0.4f)
                .height(navigationBarHeight)
                .shadow(navigationBarShadow, shape = RoundedCornerShape(doubleExtraLarge))
                .clip(shape = RoundedCornerShape(doubleExtraLarge))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (currentBackStackEntry?.destination?.route == Route.HomeScreenRoute.route || currentBackStackEntry?.destination?.route == Route.TrackingScreenRoute.route) {
                        Canvas(
                            modifier = Modifier.size(small)
                        ) {
                            drawCircle(
                                color = selectedPageColor,
                                radius = 22.dp.toPx(),
                            )
                        }
                    }

                    IconButton(onClick = {
                        if (currentBackStackEntry?.destination?.route != Route.HomeScreenRoute.route) {
                            navController.navigate(Route.HomeScreenRoute.route)
                            hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                            mainEvent(MainEvent.ShowFloatingActionButton(true))
                        }
                    }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = stringResource(R.string.navigationbar_home_icon_content_desc),
                            modifier = Modifier.size(large),
                            tint = if (currentBackStackEntry?.destination?.route == Route.HomeScreenRoute.route || currentBackStackEntry?.destination?.route == Route.TrackingScreenRoute.route)
                                MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    if (currentBackStackEntry?.destination?.route == Route.StatsScreenRoute.route) {
                        Canvas(
                            modifier = Modifier.size(small)
                        ) {
                            drawCircle(
                                color = selectedPageColor,
                                radius = 22.dp.toPx(),
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            if (currentBackStackEntry?.destination?.route != Route.StatsScreenRoute.route) {
                                navController.navigate(Route.StatsScreenRoute.route)
                                hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                                mainEvent(MainEvent.ShowFloatingActionButton(false))
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ShowChart,
                            contentDescription = stringResource(R.string.navigationbar_stats_icon_content_desc),
                            modifier = Modifier.size(large),
                            tint = if (currentBackStackEntry?.destination?.route == Route.StatsScreenRoute.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {

                    if (currentBackStackEntry?.destination?.route == Route.HistoryScreenRoute.route) {
                        Canvas(
                            modifier = Modifier.size(small)
                        ) {
                            drawCircle(
                                color = selectedPageColor,
                                radius = 22.dp.toPx(),
                            )
                        }
                    }
                IconButton(onClick = {
                    if (currentBackStackEntry?.destination?.route != Route.HistoryScreenRoute.route) {
                        navController.navigate(Route.HistoryScreenRoute.route)
                        hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                        mainEvent(MainEvent.ShowFloatingActionButton(false))
                    }
                }) {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = stringResource(R.string.navigationbar_history_icon_content_desc),
                        modifier = Modifier.size(large),
                        tint = if (currentBackStackEntry?.destination?.route == Route.HistoryScreenRoute.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            }
        }

        AnimatedVisibility(
            visible = isActionButtonShown,
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 200
                )
            ),
            exit = shrinkOut(
                animationSpec = tween(
                    durationMillis = 200,
                    delayMillis = 100
                ),
                shrinkTowards = Alignment.Center
            )
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(),
                onClick = {
                    onClickFloatingActionButton()
                    hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                },
                shape = FloatingActionButtonDefaults.extendedFabShape,
                modifier = Modifier
                    .padding(horizontal = small)
                    .scale(if (isActionButtonShown) animatedFloat.value else animatedFloatReverse.value)
                    .size(floatingActionButtonSize),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.DirectionsRun,
                    contentDescription = stringResource(R.string.home_walk_icon),
                    modifier = Modifier.size(large),
                )
            }
        }
    }
}


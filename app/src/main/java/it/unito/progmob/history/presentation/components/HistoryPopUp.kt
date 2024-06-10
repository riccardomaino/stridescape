package it.unito.progmob.history.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils.firstLocationPoint
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HistoryPopUp(
    modifier: Modifier = Modifier,
    walkToShow: WalkWithPathPoints?,
    backHandler: () -> Unit,
    onClick: () -> Unit
) {
    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var mapCenter by remember { mutableStateOf(Offset(0f, 0f)) }
    var isMapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                compassEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }

    BackHandler {

    }

    LaunchedEffect(walkToShow) {
        isMapLoaded = false
        delay(500)
        isMapLoaded = true
    }

    LaunchedEffect(key1 = walkToShow) {
        delay(1000)
        walkToShow?.pathPoints?.firstLocationPoint()?.let { LatLng(it.lat, it.lng) }?.let {
            zoomToCurrentPosition(
                coroutineScope = coroutineScope,
                cameraPositionState = cameraPositionState,
                latLng = it
            )
        }
    }
    AnimatedContent(
        modifier = modifier,
        targetState = walkToShow,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "",
    ) { targetWalk ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetWalk != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                )
                Column(
                    modifier = modifier
                        .padding(medium)
                        .shadow(medium, shape = RoundedCornerShape(medium))
                        .clickable { onClick() }
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${targetWalk.walkId}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(medium))
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .clip(RoundedCornerShape(medium))
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                    ) {
                        if(isMapLoaded) {
                            ShowMapLoadingProgressBar()
                            GoogleMap(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .drawBehind {
                                        mapSize = size
                                        mapCenter = center
                                    },
                                uiSettings = mapUiSettings,
                                cameraPositionState = cameraPositionState,
                            ) {
                                DrawHistoryPathPoints(
                                    pathPoints = targetWalk.pathPoints
                                )
                            }
                        }
                    }
                    Column(
                        modifier = modifier
                            .padding(horizontal = small)
                            .fillMaxSize()
                            .sharedElement(
                                state = rememberSharedContentState(key = targetWalk.walkId),
                                animatedVisibilityScope = this@AnimatedContent,
                            )
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.spacedBy(
                            medium,
                            Alignment.CenterVertically
                        )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = targetWalk.steps.toString(),
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Steps",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        HorizontalDivider(modifier.fillMaxWidth())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(bottom = medium)

                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = targetWalk.calories.toString(),
                                    title = "Calories",
                                    icon = Icons.Outlined.LocalFireDepartment,
                                    iconDescription = "Localized description",
                                    iconColor = Color.Red
                                )
                            }

                            VerticalDivider(
                                modifier = modifier.height(doubleExtraLarge)
                            )

                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = targetWalk.distance.toString(),
                                    title = "Km.",
                                    icon = Icons.Outlined.Map,
                                    iconDescription = "Localized description",
                                    iconColor = Color(0xFF0C9B12)
                                )
                            }

                            VerticalDivider(
                                modifier = modifier.height(doubleExtraLarge)
                            )

                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = TimeUtils.formatMillisTimeHoursMinutes(targetWalk.time),
                                    title = "Time",
                                    icon = Icons.Outlined.Timer,
                                    iconDescription = "Localized description",
                                    iconColor = Color(0xFFFF9800)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun BoxScope.ShowMapLoadingProgressBar(
    isMapLoaded: Boolean = false
) {
    AnimatedVisibility(
        modifier = Modifier
            .matchParentSize(),
        visible = !isMapLoaded,
        enter = EnterTransition.None,
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
        )
    }
}

private fun zoomToCurrentPosition(
    coroutineScope: CoroutineScope,
    cameraPositionState: CameraPositionState,
    zoom: Float = 17f,
    millisAnimation: Int = 500,
    latLng: LatLng,
) {
    coroutineScope.launch {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(latLng, zoom)
            ),
            durationMs = millisAnimation
        )
    }
}

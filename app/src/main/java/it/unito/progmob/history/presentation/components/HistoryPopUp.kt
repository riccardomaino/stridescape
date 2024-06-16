package it.unito.progmob.history.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.core.domain.util.WalkUtils.firstLocationPoint
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.HistoryPopUp(
    modifier: Modifier = Modifier,
    walkToShow: WalkWithPathPoints?,
    onBackBehaviour: () -> Unit,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var isMapLoaded by remember { mutableStateOf(false) }
    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var mapCenter by remember { mutableStateOf(Offset(0f, 0f)) }
    val cameraPositionState = rememberCameraPositionState()
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                compassEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }

    AnimatedContent(
        modifier = modifier,
        targetState = walkToShow,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "Animated content",
    ) { targetState ->
        if(targetState != null){
            BackHandler {
                onBackBehaviour()
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {}
                        .background(Color.Transparent),
                )
                Column(
                    modifier = modifier
                        .padding(medium)
                        .shadow(small, shape = RoundedCornerShape(medium))
                        .clickable { onClick() }
                        .background(MaterialTheme.colorScheme.surface)
                        .clip(RoundedCornerShape(medium))
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${targetState.walkId}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(medium))
                        )
                ) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                    ) {
                        ShowMapLoadingProgressBar(isMapLoaded)
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
                            onMapLoaded = {
                                isMapLoaded = true
                                walkToShow?.pathPoints?.firstLocationPoint()?.let { LatLng(it.lat, it.lng) }?.let {
                                    zoomToCurrentPosition(
                                        coroutineScope = coroutineScope,
                                        cameraPositionState = cameraPositionState,
                                        latLng = it
                                    )
                                }
                            }
                        ) {
                            DrawHistoryPathPoints(
                                pathPoints = targetState.pathPoints
                            )
                        }
                    }
                    Column(
                        modifier = modifier
                            .padding(horizontal = small)
                            .fillMaxSize()
                            .sharedElement(
                                state = rememberSharedContentState(key = targetState.walkId),
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
                                text = TimeUtils.formatMillisTime(targetState.time),
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.tracking_time_walking_stat_title),
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
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = targetState.steps.toString(),
                                    title = stringResource(R.string.tracking_steps_walking_stat_title),
                                    icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                                    iconDescription = stringResource(R.string.tracking_time_walking_stat_icon_desc),
                                    iconColor = Color(0xFF2952BB)
                                )
                            }
                            VerticalDivider(modifier = modifier.height(doubleExtraLarge))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = targetState.calories.toString(),
                                    title = stringResource(R.string.tracking_calories_walking_stat_title),
                                    icon = Icons.Outlined.LocalFireDepartment,
                                    iconDescription = stringResource(R.string.tracking_calories_walking_stat_icon_desc),
                                    iconColor = Color.Red
                                )
                            }
                            VerticalDivider(modifier = modifier.height(doubleExtraLarge))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                WalkingStatComponent(
                                    value = WalkUtils.formatDistanceToKm(targetState.distance),
                                    title = stringResource(R.string.tracking_distance_walking_stat_title),
                                    icon = Icons.Outlined.Map,
                                    iconDescription = stringResource(R.string.tracking_distance_walking_stat_icon_desc),
                                    iconColor = Color(0xFF0C9B12)
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
    isLoaded: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier
            .matchParentSize(),
        visible = !isLoaded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            strokeWidth = 10.dp,
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

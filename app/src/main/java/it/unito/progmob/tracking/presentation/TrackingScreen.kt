package it.unito.progmob.tracking.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.tracking.presentation.components.WalkingStat
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import java.math.RoundingMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    trackingEvent: (TrackingEvent) -> Unit,
    navController: NavController,
    uiTrackingState: UiTrackingState
) {
    var switchStartStopBtn by remember { mutableStateOf(true) }
    var switchPauseResumeBtn by remember { mutableStateOf(true) }

    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var mapCenter by remember { mutableStateOf(Offset(0f, 0f)) }
    var isMapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {}
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                compassEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = large, end = large, top = small, bottom = small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.user_icon),
                )
                Text(
                    stringResource(R.string.tracking_title),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold)
                )
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings_icon)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = modifier.padding(padding)
        ) {
            ShowMapLoadingProgressBar(isMapLoaded)
            GoogleMap(
                modifier = modifier
                    .fillMaxSize()
                    .drawBehind {
                        mapSize = size
                        mapCenter = center
                    },
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true },
            )
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .clip(
                        RoundedCornerShape(topStart = large, topEnd = large)
                    )
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = large),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    WalkingStat(
                        modifier = modifier,
                        icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                        iconContentDescription = stringResource(R.string.tracking_steps_walking_stat_icon_desc),
                        color = Color.Blue,
                        title = stringResource(R.string.tracking_steps_walking_stat_title),
                        content = uiTrackingState.steps.toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.LocalFireDepartment,
                        iconContentDescription = stringResource(R.string.tracking_calories_walking_stat_icon_desc),
                        color = Color.Red,
                        title = stringResource(R.string.tracking_calories_walking_stat_title),
                        content = uiTrackingState.caloriesBurnt.toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.Map,
                        iconContentDescription = stringResource(R.string.tracking_distance_walking_stat_icon_desc),
                        title = stringResource(R.string.tracking_distance_walking_stat_title),
                        color = Color(0xFF0C9B12),
                        content = (uiTrackingState.distanceInMeters.toFloat() / 1000.toFloat()).toBigDecimal()
                            .setScale(1, RoundingMode.HALF_UP).toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.Timer,
                        iconContentDescription = stringResource(R.string.tracking_time_walking_stat_icon_desc),
                        color = Color(0xFFFF9800),
                        title = stringResource(R.string.tracking_time_walking_stat_title),
                        content = TimeUtils.formatMillisTime(uiTrackingState.timeInMillis)
                    )
                }
                if (switchStartStopBtn) {
                    Button(
                        onClick = {
                            switchStartStopBtn = !switchStartStopBtn
                            trackingEvent(TrackingEvent.StartTrackingService)
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = large, vertical = medium)
                    ) {
                        Text(
                            text = stringResource(R.string.tracking_start_btn),
                            modifier = modifier.padding(vertical = small)
                        )
                    }
                } else {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                switchStartStopBtn = !switchStartStopBtn
                                trackingEvent(TrackingEvent.StopTrackingService)
                            },
                            modifier = modifier
                                .padding(horizontal = large, vertical = medium),
                        ) {
                            Text(
                                text = stringResource(R.string.tracking_stop_btn),
                                modifier = modifier.padding(vertical = small)
                            )
                        }
                        if (switchPauseResumeBtn) {
                            Button(
                                onClick = {
                                    switchPauseResumeBtn = !switchPauseResumeBtn
                                    trackingEvent(TrackingEvent.PauseTrackingService)
                                },
                                modifier = modifier
                                    .padding(horizontal = large, vertical = medium)
                            ) {
                                Text(
                                    text = stringResource(R.string.tracking_pause_btn),
                                    modifier = modifier.padding(vertical = small)
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    switchPauseResumeBtn = !switchPauseResumeBtn
                                    trackingEvent(TrackingEvent.ResumeTrackingService)
                                },
                                modifier = modifier
                                    .padding(horizontal = large, vertical = medium)
                            ) {
                                Text(
                                    text = stringResource(R.string.tracking_resume_btn),
                                    modifier = modifier.padding(vertical = small)
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
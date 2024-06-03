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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import it.unito.progmob.R
import it.unito.progmob.tracking.presentation.components.WalkingButtons
import it.unito.progmob.tracking.presentation.components.WalkingCard
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

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
    val hapticFeedback = LocalView.current
    val coroutineScope = rememberCoroutineScope()
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

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)
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
            WalkingCard(uiTrackingState = uiTrackingState)

            if (switchStartStopBtn) {
                Button(
                    onClick = {
                        switchStartStopBtn = !switchStartStopBtn
                        trackingEvent(TrackingEvent.StartTrackingService)
                        hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = large, vertical = medium)
                ) {
                    Text(
                        text = stringResource(R.string.tracking_start_btn),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier.padding(vertical = small)
                    )
                }
            } else {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = medium),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    WalkingButtons(
                        modifier = modifier,
                        trackingEvent = trackingEvent,
                        onClick = {
                            switchStartStopBtn = !switchStartStopBtn
                            trackingEvent(TrackingEvent.StopTrackingService)
                            hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                        },
                        iconDescription = stringResource(R.string.settings_icon),
                        text = stringResource(R.string.tracking_stop_btn),
                        icon = Icons.Outlined.Stop,
                        color = MaterialTheme.colorScheme.errorContainer,
                        textColor = MaterialTheme.colorScheme.error
                    )
                    if (switchPauseResumeBtn) {
                        WalkingButtons(
                            modifier = modifier,
                            trackingEvent = trackingEvent,
                            onClick = {
                                switchPauseResumeBtn = !switchPauseResumeBtn
                                trackingEvent(TrackingEvent.PauseTrackingService)
                                hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                            },
                            iconDescription = stringResource(R.string.settings_icon),
                            text = stringResource(R.string.tracking_pause_btn),
                            icon = Icons.Outlined.Pause,
                        )
                    } else {
                        WalkingButtons(
                            modifier = modifier,
                            trackingEvent = trackingEvent,
                            onClick = {
                                switchPauseResumeBtn = !switchPauseResumeBtn
                                trackingEvent(TrackingEvent.ResumeTrackingService)
                                hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                            },
                            iconDescription = stringResource(R.string.settings_icon),
                            text = stringResource(R.string.tracking_resume_btn),
                            icon = Icons.Outlined.PlayArrow
                        )
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
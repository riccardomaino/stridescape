package it.unito.progmob.tracking.presentation

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.navigation.NavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import it.unito.progmob.R
import it.unito.progmob.core.domain.Constants.LOCATION_TRACKING_INTERVAL
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.tracking.presentation.components.DrawPathPoints
import it.unito.progmob.tracking.presentation.components.StopWalkDialog
import it.unito.progmob.tracking.presentation.components.WalkingButtons
import it.unito.progmob.tracking.presentation.components.WalkingCard
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    trackingEvent: (TrackingEvent) -> Unit,
    navController: NavController,
    uiTrackingState: UiTrackingState,
    lastKnownLocation: LatLng? = null,
    lastKnownLocationUpdatesCounter: Long,
    showStopWalkDialog: Boolean,
) {
    val context = LocalContext.current
    val hapticFeedback = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val startIntentSenderResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode != Activity.RESULT_OK) {
                navController.popBackStack()
            }
        }
    )

    // Maps variables
    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var mapCenter by remember { mutableStateOf(Offset(0f, 0f)) }
    var isMapLoaded by remember { mutableStateOf(false) }
    var followUserLocation by remember { mutableStateOf(true) }
    val cameraPositionState = rememberCameraPositionState()
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                mapToolbarEnabled = false,
                compassEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }

    LaunchedEffect(key1 = true) {
        launch(Dispatchers.Default) {
            checkLocationSetting(
                context = context,
                onDisabled = {
                    startIntentSenderResultLauncher.launch(it)
                },
                onEnabled = {
                    Log.d("TrackingScreen", "ON ENABLED: Location enabled")
                }
            )
        }
    }

    LaunchedEffect(key1 = lastKnownLocationUpdatesCounter) {
        launch(Dispatchers.Default) {
            lastKnownLocation?.let {
                zoomToCurrentPosition(
                    coroutineScope = coroutineScope,
                    cameraPositionState = cameraPositionState,
                    zoom = 17f,
                    millisAnimation = 1000,
                    latLng = it
                )
            }
            followUserLocation = true
        }
    }

    LaunchedEffect(key1 = cameraPositionState.position) {
        launch(Dispatchers.Default) {
            if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                followUserLocation = false
            }
        }
    }


    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (followUserLocation) {
                LaunchedEffect(key1 = uiTrackingState.pathPoints.lastLocationPoint()) {
                    launch(Dispatchers.Default){
                        uiTrackingState.pathPoints.lastLocationPoint()?.let {
                            zoomToCurrentPosition(
                                coroutineScope = coroutineScope,
                                cameraPositionState = cameraPositionState,
                                latLng = LatLng(it.lat, it.lng)
                            )
                        }
                    }
                }
            }
            IconButton(
                modifier = modifier
                    .padding(large)
                    .clip(RoundedCornerShape(medium))
                    .background(MaterialTheme.colorScheme.primary),
                onClick = {
                    coroutineScope.launch {
                        trackingEvent(TrackingEvent.TrackSingleLocation)
                    }
                }
            ) {
                Icon(
                    Icons.Outlined.MyLocation,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(large),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
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
                properties = MapProperties(
                    isMyLocationEnabled = false
                ),
                onMapLoaded = { isMapLoaded = true },
                onMyLocationButtonClick = {
                    coroutineScope.launch {
                        trackingEvent(TrackingEvent.TrackSingleLocation)
                    }
                    true
                }
            ) {
                DrawPathPoints(
                    pathPoints = uiTrackingState.pathPoints,
                    isTracking = uiTrackingState.isTracking
                )
            }
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .clip(
                        RoundedCornerShape(topStart = large, topEnd = large)
                    )
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                WalkingCard(uiTrackingState = uiTrackingState)

                if (!uiTrackingState.isTrackingStarted) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                trackingEvent(TrackingEvent.StartTrackingService)
                                hapticFeedback.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
                            }
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
                                coroutineScope.launch {
                                    trackingEvent(TrackingEvent.ShowStopWalkDialog(true))
                                    hapticFeedback.performHapticFeedback(
                                        HapticFeedbackConstantsCompat.CONFIRM
                                    )
                                }
                            },
                            iconDescription = stringResource(R.string.settings_icon),
                            text = stringResource(R.string.tracking_stop_btn),
                            icon = Icons.Outlined.Stop,
                            color = MaterialTheme.colorScheme.errorContainer,
                            textColor = MaterialTheme.colorScheme.error
                        )
                        if (uiTrackingState.isTracking) {
                            WalkingButtons(
                                modifier = modifier,
                                trackingEvent = trackingEvent,
                                onClick = {
                                    coroutineScope.launch {
                                        trackingEvent(TrackingEvent.PauseTrackingService)
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackConstantsCompat.CONFIRM
                                        )
                                    }
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
                                    coroutineScope.launch {
                                        trackingEvent(TrackingEvent.ResumeTrackingService)
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackConstantsCompat.CONFIRM
                                        )
                                    }
                                },
                                iconDescription = stringResource(R.string.settings_icon),
                                text = stringResource(R.string.tracking_resume_btn),
                                icon = Icons.Outlined.PlayArrow
                            )
                        }
                    }
                }
            }
            StopWalkDialog(
                trackingEvent = trackingEvent,
                shouldShowDialog = showStopWalkDialog
            )
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

private fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        LOCATION_TRACKING_INTERVAL
    ).setMinUpdateIntervalMillis(LOCATION_TRACKING_INTERVAL)
        .build()

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // Ignore the error
                Log.d("TrackingScreen", "Ignored Exception: ${sendEx.message.toString()}")
            }
        }
    }
}
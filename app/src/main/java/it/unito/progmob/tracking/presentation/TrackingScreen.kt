package it.unito.progmob.tracking.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
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
import it.unito.progmob.tracking.presentation.components.WalkingStat
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    trackingEvent: (TrackingEvent) -> Unit,
    navController: NavController
) {
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
        },
        floatingActionButton = {
//            Row {
//                FloatingActionButton(
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    elevation = FloatingActionButtonDefaults.elevation(),
//                    onClick = { },
//                    shape = FloatingActionButtonDefaults.extendedFabShape,
//                    modifier = Modifier
//                        .padding(small)
//                        .size(floatingActionButtonSize)
//                ) {
//                    Icon(
//                        Icons.Filled.Stop,
//                        contentDescription = stringResource(R.string.tracking_stop_icon),
//                        modifier = Modifier.size(large),
//                    )
//                }
//                FloatingActionButton(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    elevation = FloatingActionButtonDefaults.elevation(),
//                    onClick = { },
//                    shape = FloatingActionButtonDefaults.extendedFabShape,
//                    modifier = Modifier
//                        .padding(small)
//                        .size(floatingActionButtonSize)
//                ) {
//                    Icon(
//                        Icons.Filled.PlayArrow,
//                        contentDescription = stringResource(R.string.tracking_play_icon),
//                        modifier = Modifier.size(large),
//                    )
//                }
//
//
//            }
        },
//        bottomBar = {
//            NavigationBar(
//                floatingActionButtonIcon = {
//                    Icon(
//                        Icons.Filled.Stop,
//                        contentDescription = stringResource(R.string.tracking_stop_icon),
//                        modifier = modifier.size(large).background(Color.Transparent),
//                    )
//                },
//                onClickFloatingActionButton = {
//
//                },
////                onClickMap = {
////                    navController.navigate(Route.OnBoardingScreenRoute.route)
////                },
////                onClickHistory = { },
//                navigationController = navController
//            )
//        }
    ) { padding ->
        Box(
            modifier = modifier.padding(padding)
        ) {
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
                        iconContentDescription = " ",
                        color = Color.Blue,
                        title = "Steps",
                        content = 300.toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.LocalFireDepartment,
                        iconContentDescription = " ",
                        color = Color.Red,
                        title = "Calories",
                        content = 120.toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.Map,
                        iconContentDescription = " ",
                        title = "Km.",
                        color = Color(0xFF0C9B12),
                        content = 120.toString()
                    )
                    VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
                    WalkingStat(
                        icon = Icons.Outlined.Timer,
                        iconContentDescription = " ",
                        color = Color(0xFFFF9800),
                        title = "Time",
                        content = "00:23:00"
                    )
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = large, vertical = medium)
                ) {
                    Text(text = "Start", modifier = modifier.padding(vertical = small))
                }
//                Row {
//                    Button(onClick = { /*TODO*/ }) {}
//                    Button(onClick = { /*TODO*/ }) {}
//                }
            }

        }
    }


}
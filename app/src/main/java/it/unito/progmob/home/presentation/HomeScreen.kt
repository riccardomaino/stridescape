package it.unito.progmob.home.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.findActivity
import it.unito.progmob.core.domain.ext.hasAllPermissions
import it.unito.progmob.core.domain.ext.openAppSettings
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.core.presentation.components.NavigationBar
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.home.presentation.components.AccessFineLocationPermissionTextProvider
import it.unito.progmob.home.presentation.components.ActivityRecognitionPermissionTextProvider
import it.unito.progmob.home.presentation.components.CircularProgressBar
import it.unito.progmob.home.presentation.components.PermissionDialog
import it.unito.progmob.home.presentation.components.PostNotificationsPermissionTextProvider
import it.unito.progmob.home.presentation.components.WalkingStats
import it.unito.progmob.home.presentation.components.WeeklyStats
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeEvent: (HomeEvent) -> Unit,
    navController: NavController,
    visiblePermissionDialogQueue: List<String>,
    permissionsToRequest: Array<String>,
    currentDayOfWeek: Int,
    stepsCurrentDay: Int,
    caloriesCurrentDay: Int,
    distanceCurrentDay: Int,
    timeCurrentDay: Long
) {

    val context = LocalContext.current
    val mainActivity = context.findActivity()


    val weeklyStats = intArrayOf(300, 200, 3200, 2000, 250, 6200, 12000)
    val weeklyTargetStats = intArrayOf(6000, 6000, 6000, 6000, 6000, 6000, 6000)

//    val allPermissionsGranted = remember {
//        mutableStateOf(false)
//    }

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                if (perms[permission] == false) {
                    homeEvent(
                        HomeEvent.PermissionResult(
                            permission,
                            isGranted = perms[permission] == true
                        )
                    )
                }
            }
        }
    )


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = large, end = large, bottom = small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.user_icon),
                )
                Text(
                    stringResource(R.string.home_title),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                )
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings_icon)
                )
            }
        },
        bottomBar = {
            NavigationBar(
                floatingActionButtonIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.DirectionsRun,
                        contentDescription = stringResource(R.string.home_walk_icon),
                        modifier = Modifier.size(large),
                    )
                },
                onClickFloatingActionButton = {
                    if(context.hasAllPermissions()){
                        navController.navigate(Route.TrackingScreenRoute.route)
                    }else{
                        multiplePermissionResultLauncher.launch(
                            permissionsToRequest
                        )
                    }
//                    multiplePermissionResultLauncher.launch(
//                        permissionsToRequest
//                    )

//                    allPermissionsGranted.value = permissionsToRequest.all { permission ->
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            permission
//                        ) == PackageManager.PERMISSION_GRANTED
//                    }

//                    if (allPermissionsGranted.value) {
//                        navController.navigate(Route.TrackingScreenRoute.route)
//                    }
                },
                navigationController = navController,
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = small)
                    .shadow(small, shape = RoundedCornerShape(large))
                    .clip(shape = RoundedCornerShape(large))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                CircularProgressBar(steps = stepsCurrentDay, targetStepsGoal = 6000, radius = 88.dp)
            }
            Spacer(modifier = Modifier.height(small))
            WalkingStats(
                kcal = caloriesCurrentDay.toString(),
                km = WalkUtils.formatDistanceToKm(distanceCurrentDay),
                time = TimeUtils.formatMillisTime(timeCurrentDay)
            )
            Spacer(modifier = Modifier.height(small))
            WeeklyStats(
                selectedDay = currentDayOfWeek,
                weeklySteps = weeklyStats,
                weeklyTarget = weeklyTargetStats
            )
        }
    }

    visiblePermissionDialogQueue.reversed().forEach { permission ->
        PermissionDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.ACTIVITY_RECOGNITION -> ActivityRecognitionPermissionTextProvider()
                Manifest.permission.ACCESS_FINE_LOCATION -> AccessFineLocationPermissionTextProvider()
                Manifest.permission.POST_NOTIFICATIONS -> PostNotificationsPermissionTextProvider()
                else -> return@forEach
            },
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                mainActivity,
                permission
            ),
            onDismiss = {
                homeEvent(HomeEvent.DismissPermissionDialog)
            },
            onOkClick = {
                homeEvent(HomeEvent.DismissPermissionDialog)
                multiplePermissionResultLauncher.launch(
                    arrayOf(permission)
                )
            },
            onGoToAppSettingsClick = {
                mainActivity.openAppSettings()
            }
        )
    }
}
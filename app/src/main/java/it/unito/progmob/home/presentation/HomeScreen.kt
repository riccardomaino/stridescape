package it.unito.progmob.home.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.findActivity
import it.unito.progmob.core.presentation.components.NavigationBar
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.home.domain.util.openAppSettings
import it.unito.progmob.home.presentation.components.AccessFineLocationPermissionTextProvider
import it.unito.progmob.home.presentation.components.ActivityRecognitionPermissionTextProvider
import it.unito.progmob.home.presentation.components.CircularProgressBar
import it.unito.progmob.home.presentation.components.PermissionDialog
import it.unito.progmob.home.presentation.components.PostNotificationsPermissionTextProvider
import it.unito.progmob.home.presentation.viewmodel.HomeViewModel
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeEvent: (HomeEvent) -> Unit,
    homeViewModel: HomeViewModel,
    navController: NavController
) {

    val dialogQueue by homeViewModel.visiblePermissionDialogQueue.collectAsState()


    val permissionsToRequest = homeViewModel.permissionsToRequest
    val context = LocalContext.current
    val mainActivity = context.findActivity()
    val steps by homeViewModel.stepsCount.collectAsState()

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
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = large, end = large, top = small, bottom = small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.home_user_icon),
                )
                Text(
                    stringResource(R.string.home_title),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold)
                )
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.home_settings_icon)
                )
            }
        },
        bottomBar = {
            NavigationBar(
                onClickHome = {},
                onClickMap = {
                    navController.navigate(Route.OnBoardingScreenRoute.route)
                },
                onClickHistory = { },
                onClickPlay = {
                    multiplePermissionResultLauncher.launch(
                        permissionsToRequest
                    )
                },
            )

        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            CircularProgressBar(steps = steps, targetStepsGoal = 6000)
        }

        dialogQueue.reversed().forEach { permission ->
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
}
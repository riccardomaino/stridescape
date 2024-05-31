package it.unito.progmob.core

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.allPermissions
import it.unito.progmob.core.domain.ext.hasAllPermissions
import it.unito.progmob.core.domain.ext.openAppSettings
import it.unito.progmob.core.presentation.MainEvent
import it.unito.progmob.core.presentation.components.AccessFineLocationPermissionTextProvider
import it.unito.progmob.core.presentation.components.ActivityRecognitionPermissionTextProvider
import it.unito.progmob.core.presentation.components.NavigationBar
import it.unito.progmob.core.presentation.components.PermissionDialog
import it.unito.progmob.core.presentation.components.PostNotificationsPermissionTextProvider
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.core.presentation.viewmodel.MainViewModel
import it.unito.progmob.ui.theme.MyApplicationTheme
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        installSplashScreen().apply {
            // Check if the boolean is true at every frame, it shows the splash screen until
            // the condition is false
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val startDestination = mainViewModel.startDestination
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val visiblePermissionDialogQueue by mainViewModel.visiblePermissionDialogQueue.collectAsState()
                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        context.allPermissions.forEach { permission ->
                            if (perms[permission] == false) {
                                mainViewModel.onEvent(
                                    MainEvent.PermissionResult(
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
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    topBar = {
                        if (currentBackStackEntry?.destination?.route != Route.OnBoardingScreenRoute.route) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = large, end = large, bottom = small),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if(currentBackStackEntry?.destination?.route != Route.OnBoardingProfileScreenRoute.route){
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = stringResource(R.string.user_icon),
                                    )
                                }
                                when (currentBackStackEntry?.destination?.route) {
                                    Route.OnBoardingProfileScreenRoute.route -> Text(
                                        stringResource(R.string.onboparding_page4_title),
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Route.HomeScreenRoute.route -> Text(
                                        stringResource(R.string.home_title),
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Route.StatsScreenRoute.route -> Text(
                                        stringResource(R.string.stats_title),
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Route.TrackingScreenRoute.route -> Text(
                                        stringResource(R.string.tracking_title),
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                if(currentBackStackEntry?.destination?.route != Route.OnBoardingProfileScreenRoute.route){
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = stringResource(R.string.settings_icon)
                                    )
                                }
                            }
                        }
                    },
                    bottomBar = {
                        Log.d("MainActivity", "current entry: ${currentBackStackEntry?.destination?.route}")
                        when (currentBackStackEntry?.destination?.route) {
                            Route.OnBoardingScreenRoute.route -> {}
                            Route.OnBoardingProfileScreenRoute.route -> {}
                            Route.TrackingScreenRoute.route -> {}
                            else -> {
                                NavigationBar(
                                    onClickFloatingActionButton = {
                                        if (context.hasAllPermissions()) {
                                            navController.navigate(Route.TrackingScreenRoute.route)
                                        } else {
                                            multiplePermissionResultLauncher.launch(
                                                context.allPermissions
                                            )
                                            if (context.hasAllPermissions()) {
                                                navController.navigate(Route.TrackingScreenRoute.route)
                                            }
                                        }
                                    },
                                    currentBackStackEntry = currentBackStackEntry,
                                    navController = navController
                                )
                            }
                        }
                    }

                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        NavGraph(
                            startDestination = startDestination,
                            navController = navController
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
                        isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            permission
                        ),
                        onDismiss = {
                            mainViewModel.onEvent(MainEvent.DismissPermissionDialog)
                        },
                        onOkClick = {
                            mainViewModel.onEvent(MainEvent.DismissPermissionDialog)
                            multiplePermissionResultLauncher.launch(
                                arrayOf(permission)
                            )
                        },
                        onGoToAppSettingsClick = {
                            this.openAppSettings()
                        }
                    )
                }

            }
        }
    }
}
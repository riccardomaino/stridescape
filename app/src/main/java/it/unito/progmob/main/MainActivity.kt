package it.unito.progmob.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import it.unito.progmob.core.presentation.components.NavigationBar
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.main.presentation.MainEvent
import it.unito.progmob.main.presentation.components.AccessFineLocationPermissionTextProvider
import it.unito.progmob.main.presentation.components.ActivityRecognitionPermissionTextProvider
import it.unito.progmob.main.presentation.components.PermissionDialog
import it.unito.progmob.main.presentation.components.PostNotificationsPermissionTextProvider
import it.unito.progmob.main.presentation.viewmodel.MainViewModel
import it.unito.progmob.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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
                val isActionButtonShown by mainViewModel.isActionButtonShown.collectAsState()
                var isTopBarVisible by remember { mutableStateOf(true) }
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
                        when (currentBackStackEntry?.destination?.route) {
                            Route.OnBoardingScreenRoute.route -> {
                                isTopBarVisible = false
                            }

                            Route.OnBoardingProfileScreenRoute.route -> {
                                isTopBarVisible = false
                            }

                            Route.ProfileScreenRoute.route -> {
                                isTopBarVisible = false
                            }

                            else -> {
                                TopAppBar(
                                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    title = {
                                        when (currentBackStackEntry?.destination?.route) {
                                            Route.OnBoardingProfileScreenRoute.route -> Text(
                                                stringResource(R.string.onboparding_page4_title),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            Route.HomeScreenRoute.route -> Text(
                                                stringResource(R.string.home_title),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            Route.StatsScreenRoute.route -> Text(
                                                stringResource(R.string.stats_title),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            Route.TrackingScreenRoute.route -> Text(
                                                stringResource(R.string.tracking_title),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            Route.HistoryScreenRoute.route -> Text(
                                                stringResource(R.string.history_title),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    },
                                    actions = {
                                        when (currentBackStackEntry?.destination?.route) {
                                            Route.TrackingScreenRoute.route -> {}
                                            else -> {
                                                IconButton(
                                                    onClick = {
                                                        navController.navigate(Route.ProfileScreenRoute.route)
                                                        mainViewModel.onEvent(
                                                            MainEvent.ShowFloatingActionButton(
                                                                false
                                                            )
                                                        )
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Default.Person,
                                                        contentDescription = stringResource(R.string.topappbar_user_icon_content_desc),
                                                        tint = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    },
                    bottomBar = {
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
                                            multiplePermissionResultLauncher.launch(context.allPermissions)
                                        }
                                    },
                                    currentBackStackEntry = currentBackStackEntry,
                                    navController = navController,
                                    isActionButtonShown = isActionButtonShown,
                                    mainEvent = mainViewModel::onEvent
                                )
                            }
                        }
                    }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .padding(padding),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        NavGraph(
                            startDestination = startDestination,
                            navController = navController,
                            mainEvent = mainViewModel::onEvent
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
package it.unito.progmob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.viewmodel.MainViewModel
import it.unito.progmob.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        installSplashScreen().apply {
            // Check if the boolean is true at every frame, it shows the splash screen until
            // the condition is false
            setKeepOnScreenCondition{
                !mainViewModel.isReady.value
            }
        }
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                val startDestination = mainViewModel.startDestination
                Surface {
                    NavGraph(
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
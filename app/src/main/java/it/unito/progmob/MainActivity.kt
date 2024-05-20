package it.unito.progmob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.viewmodel.MainViewModel
import it.unito.progmob.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                val startDestination = viewModel.startDestination
                Surface {
                    NavGraph(
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
package it.unito.progmob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.core.presentation.navigation.NavGraph
import it.unito.progmob.core.presentation.viewmodel.MainViewModel
import it.unito.progmob.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class  MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = viewModel.startDestination
                    NavGraph(
                        startDestination = startDestination,
                        mainActivity = this
                    )
                }
            }
        }
    }
}
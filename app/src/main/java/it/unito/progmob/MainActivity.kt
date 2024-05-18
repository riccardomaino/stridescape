package it.unito.progmob

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint
import it.unito.progmob.core.domain.service.RunningService
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

                    Column {
                        Button(
                            onClick = {
                                Intent(applicationContext, RunningService::class.java).also {
                                    it.action = RunningService.Actions.START.name
                                    startService(it)
                                }
                            }
                        ) { Text(text = "Start Service") }
                        Button(
                            onClick = {
                                Intent(RunningService.Actions.STOP.name).also {
                                    startService(it)
                                }
                            }
                        ) { Text(text = "Stop Service") }
                    }

                }
            }
        }
    }
}
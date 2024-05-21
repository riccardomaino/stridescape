package it.unito.progmob.tracking.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.presentation.components.NavigationBar
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.tracking.presentation.viewmodel.TrackingViewModel
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small

@Composable
fun TrackingScreen(
    modifier: Modifier = Modifier,
    trackingEvent: (TrackingEvent) -> Unit,
    trackingViewModel: TrackingViewModel,
    navController: NavController
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
        bottomBar = {
            NavigationBar(
                floatingActionButtonIcon = {
                    Icon(
                        Icons.Filled.Stop,
                        contentDescription = stringResource(R.string.stop_icon),
                        modifier = modifier.size(large),
                    )
                },
                onClickFloatingActionButton = {

                },
                onClickHome = {},
                onClickMap = {
                    navController.navigate(Route.OnBoardingScreenRoute.route)
                },
                onClickHistory = { },
            )
        }
    ){
        padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = "Tracking Screen")
        }
    }

}
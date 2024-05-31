package it.unito.progmob.stats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import it.unito.progmob.stats.presentation.components.StatsFilter
import it.unito.progmob.stats.presentation.state.UiStatsState


@Composable
fun StatsScreen(
    statsEvent: (StatsEvent) -> Unit,
    uiStatsState: UiStatsState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        StatsFilter(
            selectedFilter = uiStatsState.statsSelected,
            statsEvent = statsEvent
        )
    }
}
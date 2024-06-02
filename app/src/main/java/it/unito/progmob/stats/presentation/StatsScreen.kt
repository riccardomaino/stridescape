package it.unito.progmob.stats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.navigation.NavController
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.components.StatsChart
import it.unito.progmob.stats.presentation.components.StatsFilter
import it.unito.progmob.stats.presentation.state.UiStatsState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small


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
        Spacer(modifier = Modifier.height(medium))

        ElevatedCard(
            modifier = modifier
                .padding(horizontal = medium)
                .shadow(small, shape = RoundedCornerShape(large)),
        ) {
            if (uiStatsState.statsSelected == StatsType.DISTANCE && uiStatsState.distanceChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.TIME && uiStatsState.timeChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.CALORIES && uiStatsState.caloriesChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.STEPS && uiStatsState.stepsChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.SPEED && uiStatsState.speedChartValues.isEmpty()){
                CircularProgressIndicator()
            }else{
                StatsChart(uiStatsState = uiStatsState)
            }

        }
    }
}
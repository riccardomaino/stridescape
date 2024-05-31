package it.unito.progmob.stats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import it.unito.progmob.stats.presentation.components.StatsChart
import it.unito.progmob.stats.presentation.components.StatsFilter
import it.unito.progmob.stats.presentation.state.UiStatsState
import it.unito.progmob.ui.theme.medium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.random.Random


@Composable
fun StatsScreen(
    statsEvent: (StatsEvent) -> Unit,
    uiStatsState: UiStatsState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val TRANSACTION_INTERVAL_MS = 2000L

    LaunchedEffect(uiStatsState) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.tryRunTransaction { columnSeries { series(List(47) { 2 + Random.nextFloat() * 18 }) } }
                delay(TRANSACTION_INTERVAL_MS)
            }
        }
    }

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
        StatsChart(modelProducer = modelProducer)
    }
}
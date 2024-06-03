package it.unito.progmob.stats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.DateUtils
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
        Spacer(modifier = modifier.height(small))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = small)
                .shadow(small, shape = RoundedCornerShape(large))
                .clip(shape = RoundedCornerShape(large))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (uiStatsState.statsSelected == StatsType.DISTANCE && uiStatsState.distanceChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.TIME && uiStatsState.timeChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.CALORIES && uiStatsState.caloriesChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.STEPS && uiStatsState.stepsChartValues.isEmpty() ||
                uiStatsState.statsSelected == StatsType.SPEED && uiStatsState.speedChartValues.isEmpty()
            ) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = modifier.padding(horizontal = medium, vertical = small)
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
//                            modifier = modifier.padding(top = small, start = medium),
                            text = stringResource(R.string.stats_chart_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
//                            modifier = modifier.padding(top = small, start = medium),
                            text = " ${
                                DateUtils.formatDateFromEpochMillis(
                                    uiStatsState.startDate,
                                    DateUtils.chartFormatter
                                )
                            } - ${
                                DateUtils.formatDateFromEpochMillis(
                                    uiStatsState.endDate,
                                    DateUtils.chartFormatter
                                )
                            }",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                    HorizontalDivider(
                        modifier = modifier.padding(
                            vertical = small,
//                            horizontal = small
                        )
                    )
                    StatsChart(uiStatsState = uiStatsState)
                }
            }
        }

    }
}
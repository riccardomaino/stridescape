package it.unito.progmob.stats.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.fullMonthsNames
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.components.RangeFilter
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
    isDataLoaded: Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val fullMonthsNames = rememberSaveable { context.fullMonthsNames }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalArrangement = Arrangement.Top
    ) {
        StatsFilter(
            statsSelected = uiStatsState.statsSelected,
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
            Column(
                modifier = modifier.padding(horizontal = medium, vertical = small)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier
                            .clip(shape = RoundedCornerShape(medium))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = medium)
                    ) {
                        Text(
                            text = when (uiStatsState.statsSelected) {
                                StatsType.DISTANCE -> stringResource(R.string.stats_chart_y_axis_distance_title)
                                StatsType.TIME -> stringResource(R.string.stats_chart_y_axis_time_title)
                                StatsType.CALORIES -> stringResource(R.string.stats_chart_y_axis_calories_title)
                                StatsType.STEPS -> stringResource(R.string.stats_chart_y_axis_steps_title)
                                StatsType.SPEED -> stringResource(R.string.stats_chart_y_axis_speed_title)
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                    Text(
                        text = when (uiStatsState.rangeSelected) {
                            RangeType.WEEK -> "${DateUtils.formattedFirstDateOfWeek()} - ${DateUtils.formattedLastDateOfWeek()}"
                            RangeType.MONTH -> fullMonthsNames[DateUtils.getCurrentMonth() - 1].uppercase()
                            RangeType.YEAR -> DateUtils.getCurrentYear().toString()
                        },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                HorizontalDivider(modifier = modifier.padding(vertical = small))

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(Color.Transparent)
                ) {
                    ShowLoadingProgressIndicator(isLoaded = isDataLoaded)
                    if (isDataLoaded) {
                        StatsChart(uiStatsState = uiStatsState)
                    }
                }
                RangeFilter(
                    modifier = modifier,
                    rangeSelected = uiStatsState.rangeSelected,
                    statsEvent = statsEvent
                )
            }
        }
    }

}


@Composable
private fun BoxScope.ShowLoadingProgressIndicator(
    isLoaded: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier.matchParentSize(),
        visible = !isLoaded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            strokeWidth = 10.dp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
        )
    }
}
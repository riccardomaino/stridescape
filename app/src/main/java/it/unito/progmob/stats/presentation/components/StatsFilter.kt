package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.leftFadingEdge
import it.unito.progmob.core.domain.ext.rightFadingEdge
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.medium

@Composable
fun StatsFilter(
    statsSelected: StatsType,
    statsEvent: (StatsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberScrollState()

    Row(
        horizontalArrangement = Arrangement.spacedBy(medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = medium)
            .leftFadingEdge(
                color = MaterialTheme.colorScheme.surfaceVariant,
                width = medium,
                spec = tween(250),
                isVisible = listState.canScrollBackward,
            )
            .rightFadingEdge(
                color = MaterialTheme.colorScheme.surfaceVariant,
                width = medium,
                spec = tween(250),
                isVisible = listState.canScrollForward
            )
            .horizontalScroll(listState),
    ) {
        StatsType.entries.forEach {
            ElevatedFilterChip(
                onClick = { statsEvent(StatsEvent.StatsTypeSelected(it)) },
                selected = it == statsSelected,
                shape = RoundedCornerShape(medium),
                leadingIcon = {
                    AnimatedVisibility(it == statsSelected) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.stats_selected_filter_icon_content_description),
                        )
                    }
                },
                label = {
                    Text(
                        text = when (it) {
                            StatsType.DISTANCE -> stringResource(R.string.stats_stats_filter_distance_text)
                            StatsType.TIME -> stringResource(R.string.stats_stats_filter_time_text)
                            StatsType.CALORIES -> stringResource(R.string.stats_stats_filter_calories_text)
                            StatsType.STEPS -> stringResource(R.string.stats_stats_filter_steps_text)
                            StatsType.SPEED -> stringResource(R.string.stats_stats_filter_speed_text)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }

}
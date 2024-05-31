package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.medium

@Composable
fun StatsFilter(
    selectedFilter: StatsType,
    statsEvent: (StatsEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(bottom = medium)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(medium)
    ) {
        StatsType.entries.forEach {
            ElevatedFilterChip(
                onClick = { statsEvent(StatsEvent.FilterSelected(it)) },
                selected = it == selectedFilter,
                leadingIcon = {
                    AnimatedVisibility(it == selectedFilter) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.stats_selected_filter_icon_content_description),
                        )
                    }
                },
                label = {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.medium

@Composable
fun RangeFilter(
    modifier: Modifier = Modifier,
    rangeSelected: RangeType,
    statsEvent: (StatsEvent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(medium, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RangeType.entries.forEach {
            ElevatedFilterChip(
                onClick = { statsEvent(StatsEvent.RangeTypeSelected(it)) },
                selected = it == rangeSelected,
                shape = RoundedCornerShape(medium),
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondary
                ),
                leadingIcon = {
                    AnimatedVisibility(it == rangeSelected) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.stats_selected_filter_icon_content_description),
                        )
                    }
                },
                label = {
                    Text(
                        text = when (it) {
                            RangeType.WEEK -> stringResource(R.string.stats_range_filter_week)
                            RangeType.MONTH -> stringResource(R.string.stats_range_filter_month)
                            RangeType.YEAR -> stringResource(R.string.stats_range_filter_year)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
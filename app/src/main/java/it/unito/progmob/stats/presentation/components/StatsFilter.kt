package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsFilter(
    statsSelected: StatsType,
    rangeSelected: RangeType,
    statsEvent: (StatsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = medium),
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(medium),
            verticalAlignment = Alignment.CenterVertically
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
                            text = it.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
            }
        }

    }
}
package it.unito.progmob.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun WalkTile(
    modifier: Modifier = Modifier,
    walk: WalkWithPathPoints,
    onClick: () -> Unit = {}
) {
    val color = MaterialTheme.colorScheme.surface
    Box(
        modifier = modifier
            .padding(vertical = small)
            .clip(RoundedCornerShape(medium))
            .background(color)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WalkTileItem(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                iconColor = MaterialTheme.colorScheme.primary,
                value = walk.steps.toString(),
                contentDescription = stringResource(R.string.tracking_steps_walking_stat_icon_desc),
                modifier = Modifier
            )
            WalkTileItem(
                icon = Icons.Outlined.LocalFireDepartment,
                iconColor = Color.Red,
                value = walk.calories.toString(),
                contentDescription = stringResource(R.string.tracking_calories_walking_stat_icon_desc),
                modifier = Modifier
            )
            WalkTileItem(
                icon = Icons.Outlined.Map,
                iconColor = Color(0xFF0C9B12),
                value = WalkUtils.formatDistanceToKm(walk.distance),
                contentDescription = stringResource(R.string.tracking_distance_walking_stat_icon_desc),
                modifier = Modifier
            )
            WalkTileItem(
                icon = Icons.Outlined.Timer,
                iconColor = Color(0xFFFF9800),
                value = TimeUtils.formatMillisTimeHoursMinutes(walk.time),
                contentDescription = stringResource(R.string.tracking_time_walking_stat_icon_desc),
                modifier = Modifier
            )
        }

    }
}
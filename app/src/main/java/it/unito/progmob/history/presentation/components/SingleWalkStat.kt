package it.unito.progmob.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun SingleWalkStat(
    modifier: Modifier = Modifier,
    singleWalk: WalkWithPathPoints,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(vertical = small)
            .clip(RoundedCornerShape(medium))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SingleStatBlock(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                iconColor = MaterialTheme.colorScheme.primary,
                value = singleWalk.steps.toString(),
                contentDescription = "Total steps of the walk",
                modifier = modifier
            )
            SingleStatBlock(
                icon = Icons.Outlined.LocalFireDepartment,
                iconColor = Color.Red,
                value = singleWalk.calories.toString(),
                contentDescription = "Total calories of the walk",
                modifier = modifier
            )
            SingleStatBlock(
                icon = Icons.Outlined.Map,
                iconColor = Color(0xFF0C9B12),
                value = singleWalk.distance.toString(),
                contentDescription = "Total kilometers of the walk",
                modifier = modifier
            )
            SingleStatBlock(
                icon = Icons.Outlined.Timer,
                iconColor = Color(0xFFFF9800),
                value = TimeUtils.formatMillisTimeHoursMinutes(singleWalk.time),
                contentDescription = "Total duration of the walk",
                modifier = modifier
            )
        }

    }
}
package it.unito.progmob.tracking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.large

@Composable
fun WalkingCard(modifier: Modifier = Modifier, uiTrackingState: UiTrackingState) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = large),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        WalkingStat(
            modifier = modifier,
            icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
            iconContentDescription = stringResource(R.string.tracking_steps_walking_stat_icon_desc),
            color = Color(0xFF2952BB),
            title = stringResource(R.string.tracking_steps_walking_stat_title),
            content = uiTrackingState.steps.toString()
        )
        VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
        WalkingStat(
            icon = Icons.Outlined.LocalFireDepartment,
            iconContentDescription = stringResource(R.string.tracking_calories_walking_stat_icon_desc),
            color = Color.Red,
            title = stringResource(R.string.tracking_calories_walking_stat_title),
            content = uiTrackingState.caloriesBurnt.toString()
        )
        VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
        WalkingStat(
            icon = Icons.Outlined.Map,
            iconContentDescription = stringResource(R.string.tracking_distance_walking_stat_icon_desc),
            title = stringResource(R.string.tracking_distance_walking_stat_title),
            color = Color(0xFF0C9B12),
            content = WalkUtils.formatDistanceToKm(uiTrackingState.distanceInMeters)
        )
        VerticalDivider(modifier = Modifier.height(doubleExtraLarge))
        WalkingStat(
            icon = Icons.Outlined.Timer,
            iconContentDescription = stringResource(R.string.tracking_time_walking_stat_icon_desc),
            color = Color(0xFFFF9800),
            title = stringResource(R.string.tracking_time_walking_stat_title),
            content = TimeUtils.formatMillisTime(uiTrackingState.timeInMillis)
        )
    }
}
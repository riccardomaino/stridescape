package it.unito.progmob.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun WalkingStats(
    modifier: Modifier = Modifier,
    kcal: Int,
    km: Int,
    time: Long
) {
    var kcalAnimationPlayed by remember {
        mutableStateOf(false)
    }
    var kmAnimationPlayed by remember {
        mutableStateOf(false)
    }
    var timeAnimationPlayed by remember {
        mutableStateOf(false)
    }


    val kcalAnimation = animateIntAsState(
        targetValue = if(kcalAnimationPlayed) kcal else 0,
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 0
        ), label = "kcal animation"
    )

    val kmAnimation = animateIntAsState(
        targetValue = if(kmAnimationPlayed) km else 0,
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 0
        ), label = "km animation"
    )

    val timeAnimation = animateIntAsState(
        targetValue = if(timeAnimationPlayed) time.toInt() else 0,
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 0
        ), label = "km animation"
    )

    LaunchedEffect(true) {
        kcalAnimationPlayed = true
        kmAnimationPlayed = true
        timeAnimationPlayed = true
    }

    Box(
        modifier = modifier
            .padding(small)
            .shadow(small, shape = RoundedCornerShape(large))
            .clip(RoundedCornerShape(large))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = small, vertical = medium)
        ) {
            Text(
                modifier = modifier.padding(start = medium),
                text = stringResource(R.string.home_walking_stats_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(modifier = modifier.padding(vertical = small, horizontal = small))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    WalkingStatComponent(
                        value = kcalAnimation.value.toString(),
                        title = "Calories",
                        icon = Icons.Outlined.LocalFireDepartment,
                        iconDescription = "Localized description",
                        iconColor = Color.Red
                    )
                }

                VerticalDivider(
                    modifier = modifier.height(doubleExtraLarge)
                )

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    WalkingStatComponent(
                        value = WalkUtils.formatDistanceToKm(kmAnimation.value),
                        title = "Km.",
                        icon = Icons.Outlined.Map,
                        iconDescription = "Localized description",
                        iconColor = Color(0xFF0C9B12)
                    )
                }

                VerticalDivider(
                    modifier = modifier.height(doubleExtraLarge)
                )

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                    WalkingStatComponent(
                        value = TimeUtils.formatMillisTimeHoursMinutes(timeAnimation.value.toLong()),
                        title = "Time",
                        icon = Icons.Outlined.Timer,
                        iconDescription = "Localized description",
                        iconColor = Color(0xFFFF9800)
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WalkingStatsPreview() {
    //WalkingStats(kcal = 300, km = 10, time = "20:00")
}
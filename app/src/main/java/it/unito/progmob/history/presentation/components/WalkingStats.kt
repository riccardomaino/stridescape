package it.unito.progmob.history.presentation.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.R
import it.unito.progmob.ui.theme.doubleExtraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun WalkingStats(
    modifier: Modifier = Modifier,
    kcal: String,
    km: String,
    time: String
) {
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
                        value = kcal,
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
                        value = km,
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
                        value = time,
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
    WalkingStats(kcal = 300.toString(), km = 10.0.toString(), time = "20:00")
}
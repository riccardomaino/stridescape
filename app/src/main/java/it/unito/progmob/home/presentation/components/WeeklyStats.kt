package it.unito.progmob.home.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.R
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun WeeklyStats(
    modifier: Modifier = Modifier,
    weeklySteps: IntArray,
    weeklyTarget: IntArray,
    selectedDay: Int
) {
    // val fontColor = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val weekDays = listOf(
        stringResource(R.string.home_weeklystats_monday),
        stringResource(R.string.home_weeklystats_tuesday),
        stringResource(R.string.home_weeklystats_wednesday),
        stringResource(R.string.home_weeklystats_thursday),
        stringResource(R.string.home_weeklystats_friday),
        stringResource(R.string.home_weeklystats_saturday),
        stringResource(R.string.home_weeklystats_sunday)
    )
    Box(
        modifier = modifier
            .padding(horizontal = small)
            .shadow(small, shape = RoundedCornerShape(large))
            .clip(RoundedCornerShape(large))
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = small, vertical = medium)
        ) {
            Text(
                modifier = modifier.padding(start = medium),
                text = stringResource(R.string.home_weeklystats_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(modifier = modifier.padding(vertical = small, horizontal = small))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Log.d("WeeklyStats", "WeeklySteps: ${weeklySteps.contentToString()}, Weekly size: ${weeklySteps.size}")
                weekDays.forEachIndexed { index, _ ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Log.d("WeeklyStats", "Index: $index")
                        CircularProgressBar(
                            steps = if (index < weeklySteps.size) weeklySteps[index] else 0,
                            targetStepsGoal = weeklyTarget[index],
                            showStepsInfo = false,
                            radius = medium,
                            strokeWidth = small,
                            animDelay = 1000
                        )
                        Spacer(modifier = modifier.height(small))
                        Text(
                            text = weekDays[index],
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (index == selectedDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyStatsPreview() {
    WeeklyStats(
        weeklySteps = intArrayOf(300, 200, 3200, 2000, 250, 6200, 12000),
        weeklyTarget = intArrayOf(6000, 6000, 6000, 6000, 6000, 6000, 6000),
        selectedDay = 2
    )
}
package it.unito.progmob.home.presentation.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.shortWeekDaysNames
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun WeeklyStats(
    modifier: Modifier,
    weeklySteps: IntArray,
    weeklyTarget: IntArray,
    selectedDay: Int,
) {
    val context = LocalContext.current
    val weekDaysNames = remember {
        context.shortWeekDaysNames
    }

    Box(
        modifier = modifier
            .padding(horizontal = small)
            .shadow(small, shape = RoundedCornerShape(large))
            .clip(RoundedCornerShape(large))
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = small, vertical = medium)
        ) {
            Text(
                modifier = Modifier.padding(start = medium),
                text = stringResource(R.string.home_weeklystats_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = small, horizontal = small))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weekDaysNames.forEachIndexed { index, _ ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressBar(
                            steps = weeklySteps[index],
                            targetStepsGoal = weeklyTarget[index],
                            showStepsInfo = false,
                            radius = medium,
                            strokeWidth = small,
                            animDelay = 1000,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(small))
                        Text(
                            text = weekDaysNames[index],
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
        selectedDay = 2,
        modifier = Modifier
    )
}
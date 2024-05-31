package it.unito.progmob.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.home.presentation.components.CircularProgressBar
import it.unito.progmob.home.presentation.components.WalkingStats
import it.unito.progmob.home.presentation.components.WeeklyStats
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeEvent: (HomeEvent) -> Unit,
    navController: NavController,
    currentDayOfWeek: Int,
    stepsCurrentDay: Int,
    caloriesCurrentDay: Int,
    distanceCurrentDay: Int,
    timeCurrentDay: Long,
    stepsTargetCurrentDay: Int
) {
    val weeklyStats = intArrayOf(300, 200, 3200, 2000, 250, 6200, 12000)
    val weeklyTargetStats = intArrayOf(6000, 6000, 6000, 6000, 6000, 6000, 6000)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = small)
                .shadow(small, shape = RoundedCornerShape(large))
                .clip(shape = RoundedCornerShape(large))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CircularProgressBar(steps = stepsCurrentDay, targetStepsGoal = stepsTargetCurrentDay, radius = 88.dp)
        }
        Spacer(modifier = Modifier.height(small))
        WalkingStats(
            kcal = caloriesCurrentDay.toString(),
            km = WalkUtils.formatDistanceToKm(distanceCurrentDay),
            time = TimeUtils.formatMillisTime(timeCurrentDay)
        )
        Spacer(modifier = Modifier.height(small))
        WeeklyStats(
            selectedDay = currentDayOfWeek,
            weeklySteps = weeklyStats,
            weeklyTarget = weeklyTargetStats
        )
    }
}
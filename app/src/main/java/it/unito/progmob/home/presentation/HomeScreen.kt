package it.unito.progmob.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import it.unito.progmob.home.presentation.components.CircularProgressBar
import it.unito.progmob.home.presentation.components.WalkingStats
import it.unito.progmob.home.presentation.components.WeeklyStats
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentDayOfWeek: Int,
    stepsCurrentDay: Int,
    caloriesCurrentDay: Int,
    distanceCurrentDay: Int,
    timeCurrentDay: Long,
    stepsTargetCurrentDay: Int,
    weeklySteps: IntArray,
    weeklyTarget: IntArray,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = small, end = small, top = small)
                .shadow(small, shape = RoundedCornerShape(large))
                .clip(shape = RoundedCornerShape(large))
                .background(MaterialTheme.colorScheme.surface)

        ) {
            CircularProgressBar(
                steps = stepsCurrentDay,
                targetStepsGoal = stepsTargetCurrentDay,
                radius = 88.dp,
                modifier = modifier
            )

        }
        Spacer(modifier = Modifier.height(small))
        WalkingStats(
            kcal = caloriesCurrentDay,
            km = distanceCurrentDay,
            time = timeCurrentDay
        )
        Spacer(modifier = Modifier.height(small))
        WeeklyStats(
            modifier = modifier,
            weeklySteps = weeklySteps,
            weeklyTarget = weeklyTarget,
            selectedDay = currentDayOfWeek
        )
    }
}

@Composable
private fun BoxScope.ShowLoadingProgressIndicator(
    isLoaded: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier
            .matchParentSize(),
        visible = !isLoaded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            strokeWidth = 10.dp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .wrapContentSize()
        )
    }
}

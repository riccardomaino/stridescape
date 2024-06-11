package it.unito.progmob.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unito.progmob.ui.theme.large

@Composable
fun CircularProgressBar(
    steps: Int,
    targetStepsGoal: Int = 1,
    fontSize: TextUnit = 48.sp,
    radius: Dp = 96.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    colorBackground: Color = MaterialTheme.colorScheme.outlineVariant,
    strokeWidth: Dp = large,
    animDuration: Int = 1000,
    animDelay: Int = 0,
    showStepsInfo: Boolean = true,
    modifier: Modifier
) {
    var stepBarAnimationPlayed by remember {
        mutableStateOf(false)
    }
    var stepTargetAnimationPlayed by remember {
        mutableStateOf(false)
    }

    val stepCircularBarAnimation = animateFloatAsState(
        targetValue = if (stepBarAnimationPlayed) (((steps * 100) / targetStepsGoal) / 100).toFloat() else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        ), label = "Steps counter animation"
    )

    val stepTargetAnimation = animateIntAsState(
        targetValue = if (stepTargetAnimationPlayed) targetStepsGoal else 0,
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = animDelay
        ), label = "Steps target animation"
    )

    // Runs the side effect in a lifecycle-aware manner which set that the animation has already benn played
    LaunchedEffect(key1 = true) {
        stepBarAnimationPlayed = true
        stepTargetAnimationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2.8f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2.2f)) {
            drawArc(
                color = colorBackground,
                startAngle = -90f,
                sweepAngle = 360f , // radius of the circle
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        // Allows to create various types of shapes
        Canvas(modifier = Modifier.size(radius * 2.2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * stepCircularBarAnimation.value, // radius of the circle
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        if (showStepsInfo) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Steps",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
                Text(
                    text = steps.toString(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.height(70.dp)
                )
                Text(
                    text = "${(stepTargetAnimation.value)}",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

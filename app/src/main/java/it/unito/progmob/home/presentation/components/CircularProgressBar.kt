package it.unito.progmob.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.large

@Composable
fun CircularProgressBar(
    steps: Int,
    number: Int,
    fontSize: TextUnit = 56.sp,
    radius: Dp = 96.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = large,
    animDuration: Int = 1000,
    animDelay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) ((steps*100)/number)/100.toFloat() else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    // Eseguito solo alla prima composition
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .size(radius * 2.8f)
            .padding(horizontal = large)
            .clip(shape = RoundedCornerShape(extraLarge))
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        // permette di creare una forma qualsiasi
        Canvas(modifier = Modifier.size(radius * 2.2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * currentPercentage.value, //raggio mostrato nella barra
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Steps",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = (currentPercentage.value * number).toInt().toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.height(70.dp)
            )
            Text(
                text = "/${number}",
                color = MaterialTheme.colorScheme.outline,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

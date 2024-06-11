package it.unito.progmob.profile.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageBorderAnimation(
    painter: Painter,
    contentDescription: String,
    gradientColors: List<Color>,
    borderPadding: Dp = 0.dp,
    borderWidth: Float = 10f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite transition animation")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation animation"
    )
    val colorBrush = Brush.linearGradient(colors = gradientColors)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .drawBehind {
                rotate(rotationAnimation.value){
                    drawCircle(
                        brush = colorBrush,
                        style = Stroke(width = borderWidth)
                    )
                }
            }
            .padding(borderPadding)
            .clip(CircleShape)
    )
}
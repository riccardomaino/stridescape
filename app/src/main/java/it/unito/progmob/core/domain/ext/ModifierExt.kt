package it.unito.progmob.core.domain.ext

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.unito.progmob.stats.domain.model.FadeSide

fun Modifier.fadingEdge(
    vararg sides: FadeSide,
    color: Color,
    width: Dp,
    isVisible: Boolean,
    spec: AnimationSpec<Dp>?
) = composed {
    val animatedWidth = spec?.let {
        animateDpAsState(
            targetValue = if (isVisible) width else 0.dp,
            animationSpec = spec,
            label = "Fade width"
        ).value
    }
    drawWithContent {
        // Draw the content
        this@drawWithContent.drawContent()

        // Go through all provided sides
        sides.forEach { side ->
            // Get start and end gradient offsets
            val (start, end) = size.getFadeOffsets(side)

            // Define the static width
            val staticWidth = if (isVisible) width.toPx() else 0f
            // Define the final width
            val widthPx = animatedWidth?.toPx() ?: staticWidth

            // Calculate fraction based on view size
            val fraction = when(side) {
                FadeSide.LEFT, FadeSide.RIGHT -> widthPx / this.size.width
                FadeSide.BOTTOM, FadeSide.TOP -> widthPx / this.size.height
            }

            // Draw the gradient
            drawRect(
                brush = Brush.linearGradient(
                    0f to color,
                    fraction to Color.Transparent,
                    start = start,
                    end = end
                ),
                size = this.size
            )
        }
    }

}

fun Modifier.leftFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.LEFT, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.rightFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.RIGHT, color = color, width = width, isVisible = isVisible, spec = spec)
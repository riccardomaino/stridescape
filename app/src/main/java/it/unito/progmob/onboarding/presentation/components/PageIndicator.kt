package it.unito.progmob.onboarding.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import it.unito.progmob.ui.theme.pageIndicatorHeight
import it.unito.progmob.ui.theme.pageIndicatorSpacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pagesNumber: Int,
    pagerState: PagerState
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(pageIndicatorSpacing),
            modifier = modifier.height(pageIndicatorHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pagesNumber) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(
                                alpha = if (it == pagerState.currentPage) 1f else 0.3f
                            ),
                            shape = CircleShape
                        )
                )
            }
        }
        Box(
            Modifier
                .wormTransition(pagerState, MaterialTheme.colorScheme.primary)
                .size(20.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.wormTransition(
    pagerState: PagerState,
    pathColor: Color,
) = drawBehind {
    val distance = size.width + 10.dp.roundToPx()
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    val wormOffset = (scrollPosition % 1) * 2

    val xPos = scrollPosition.toInt() * distance
    val head = xPos + distance * 0f.coerceAtLeast(wormOffset - 1)
    val tail = xPos + size.width + 1f.coerceAtMost(wormOffset) * distance

    val worm = RoundRect(
        head, 0f, tail, size.height,
        CornerRadius(50f)
    )

    val path = Path().apply { addRoundRect(worm) }
    drawPath(path = path, color = pathColor)
}
package it.unito.progmob.core.domain.ext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import it.unito.progmob.stats.domain.model.FadeSide

/**
 * Returns the start and end offsets for a fading edge on the specified [side] of a
 * rectangle with this size.
 *
 * @param side The side of the rectangle to calculate the fade offsets for.
 * @return A Pair of [Offset] values representing the start and end offsets of the fading edge.
 */
fun Size.getFadeOffsets(side: FadeSide): Pair<Offset, Offset> {
    return when (side) {
        FadeSide.LEFT -> Offset.Zero to Offset(width, 0f)
        FadeSide.RIGHT -> Offset(width, 0f) to Offset.Zero
        FadeSide.BOTTOM -> Offset(0f, height) to Offset.Zero
        FadeSide.TOP -> Offset.Zero to Offset(0f, height)
    }
}
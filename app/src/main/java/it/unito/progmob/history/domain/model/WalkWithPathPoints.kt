package it.unito.progmob.history.domain.model

import androidx.compose.runtime.Immutable
import it.unito.progmob.tracking.domain.model.PathPoint

/**
 * Data class representing a walk with its associated path points.
 *
 * @param walkId The unique identifier of the walk.
 * @param weekDay The day of the week the walk took place (0 for Monday, 1 for Sunday, etc.).
 * @param date The date of the walk in the format "yyyy-MM-dd".
 * @param month The month of the walk (1 for January, 2 for February, etc.).
 * @param steps The number of steps taken during the walk.
 * @param distance The total distance covered during the walk (in meters).
 * @param time The duration of the walk (in milliseconds).
 * @param calories The number of calories burned during the walk.
 * @param averageSpeed The average speed of the walk (in meters per second).
 * @param pathPoints A list of [PathPoint] objects representing the path taken during the walk.
 */
@Immutable
data class WalkWithPathPoints(
    val walkId: Int,
    val weekDay: Int,
    val date: String,
    val month: Int,
    val steps: Int,
    val distance: Int,
    val time: Long,
    val calories: Int,
    val averageSpeed: Float,
    val pathPoints: List<PathPoint>
)
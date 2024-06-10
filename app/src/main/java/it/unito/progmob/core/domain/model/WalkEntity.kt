package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a single walk, stored in the database.
 *
 * @property id The unique identifier for the walk in the database.
 * @property weekDay The day of the week when the walk took place (0 for Monday, 1 for Tuesday, etc.).
 * @property date The date when the walk took place.
 * @property month The month when the walk took place (1 for January, 2 forFebruary, etc.).
 * @property steps The number of steps taken during the walk.
 * @property distance The total distance covered during the walk.
 * @property time The duration of the walk in milliseconds.
 * @property calories The number of calories burned during the walk.
 * @property averageSpeed The average speed during the walk.
 */
@Entity(tableName = "walks")
data class WalkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val weekDay: Int,
    val date: String,
    val month: Int,
    val steps: Int,
    val distance: Int,
    val time: Long,
    val calories: Int,
    val averageSpeed: Float
)


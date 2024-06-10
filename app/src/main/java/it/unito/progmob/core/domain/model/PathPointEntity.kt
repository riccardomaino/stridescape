package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.unito.progmob.tracking.domain.model.PathPoint

/**
 * Data class representing a single point on a recorded walking path, stored in the database.
 *
 * @property id The unique identifier for the path point in the database.
 * @property walkId The identifier of the walk this point belongs to.
 * @property pathPoint The actual path point data.
 */
@Entity(tableName = "path_points")
data class PathPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val walkId: Long,
    val pathPoint: PathPoint
)

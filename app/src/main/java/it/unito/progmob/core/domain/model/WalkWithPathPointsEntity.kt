package it.unito.progmob.core.domain.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class representing a walk with its associated path points, retrieved from the database.
 *
 * @property walk The walk data.
 * @property pathPoints The list of path points associated with the walk.
 */
data class WalkWithPathPointsEntity (
    @Embedded
    val walk: WalkEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "walkId"
    )
    val pathPoints : List<PathPointEntity>
)
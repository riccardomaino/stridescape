package it.unito.progmob.core.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class WalkWithPathPoints (
    @Embedded
    val walk: WalkEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "walkId"
    )
    val pathPoints : List<PathPointEntity>
)
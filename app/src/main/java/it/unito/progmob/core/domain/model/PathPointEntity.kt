package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.unito.progmob.tracking.domain.model.PathPoint

@Entity(tableName = "path_points")
data class PathPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val walkId: Long,
    val pathPoint: PathPoint
)

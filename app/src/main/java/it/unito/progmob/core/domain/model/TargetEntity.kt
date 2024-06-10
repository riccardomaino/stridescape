package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a daily step target, stored in the database.
 *
 * @property date The date for which the target is set, used as the primary key.
 * @property stepsTarget The step target for the given date.
 */
@Entity(tableName = "targets")
data class TargetEntity(
    @PrimaryKey val date: String,
    val stepsTarget: Int,
)
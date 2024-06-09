package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "targets")
data class TargetEntity(
    @PrimaryKey val date: String,
    val stepsTarget: Int,
)
package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "targets")
data class TargetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val stepsTarget: Int,
    val date: String
)
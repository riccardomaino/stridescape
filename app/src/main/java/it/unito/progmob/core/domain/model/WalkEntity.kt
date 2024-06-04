package it.unito.progmob.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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


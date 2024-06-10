package it.unito.progmob.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.unito.progmob.core.domain.model.Converters
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.TargetEntity
import it.unito.progmob.core.domain.model.WalkEntity

/**
 * Room database for storing walk, path point, and step target data.
 */
@Database(entities = [WalkEntity::class, PathPointEntity::class, TargetEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WalkDatabase: RoomDatabase() {
    /**
     * The Data Access Object for interacting with walk and path point data.
     */
    abstract val walkDao: WalkDao

    /**
     * The Data Access Object for interacting with step target data.
     */
    abstract val targetDao: TargetDao

    /**
     * The name of the database file.
     */
    companion object {
        const val DATABASE_NAME = "walks_db"
    }
}
package it.unito.progmob.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.unito.progmob.core.domain.model.Converters
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity

@Database(entities = [WalkEntity::class, PathPointEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WalkDatabase: RoomDatabase() {
    abstract val walkDao: WalkDao

    companion object {
        const val DATABASE_NAME = "walks_db"
    }
}
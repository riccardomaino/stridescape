package it.unito.progmob.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity

@Database(entities = [WalkEntity::class, PathPointEntity::class], version = 1)
abstract class WalkDataBase: RoomDatabase() {
    abstract val walkDao: WalkDao
}
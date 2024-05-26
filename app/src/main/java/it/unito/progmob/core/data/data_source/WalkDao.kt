package it.unito.progmob.core.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unito.progmob.core.domain.model.PathPointEntity
import it.unito.progmob.core.domain.model.WalkEntity
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import kotlinx.coroutines.flow.Flow

@Dao
interface WalkDao {

    @Transaction
    @Query("SELECT * FROM walks")
    fun getWalksWithPathPoints(): Flow<List<WalkWithPathPoints>>

    @Upsert
    suspend fun upsertNewWalk(newWalkEntity: WalkEntity): Long
    @Upsert
    suspend fun upsertNewPathPoint(newPathPointEntity: PathPointEntity)

    @Delete
    suspend fun deleteWalk(walkEntity: WalkEntity)
    @Delete
    suspend fun deleteSinglePathPoint(pathPointEntity: PathPointEntity)
    @Query("DELETE FROM path_points WHERE walkId = :walkId")
    suspend fun deleteAllPathPointsOfWalk(walkId: Int)
}
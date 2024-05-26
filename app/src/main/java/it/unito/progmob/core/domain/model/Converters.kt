package it.unito.progmob.core.domain.model

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromPathPoint(pathPoint: PathPoint): String = Json.encodeToString(pathPoint)

    @TypeConverter
    fun toPathPoint(value: String): PathPoint = Json.decodeFromString<PathPoint>(value)
}

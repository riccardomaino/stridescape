package it.unito.progmob.core.domain.model

import androidx.room.TypeConverter
import it.unito.progmob.tracking.domain.model.PathPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A utility class for converting `PathPoint` objects to and from `String` using JSON serialization.
 */
class Converters {
    /**
     * Converts a `PathPoint` object to a JSON string.
     *
     * @param pathPoint The `PathPoint` object to be converted.
     * @return The JSON string representation of the `PathPoint`.
     */
    @TypeConverter
    fun fromPathPoint(pathPoint: PathPoint): String = Json.encodeToString(pathPoint)

    /**
     * Converts a JSON string to a `PathPoint` object.
     *
     * @param value The JSON string to be converted.
     * @return The `PathPoint` object represented by the JSON string.
     */
    @TypeConverter
    fun toPathPoint(value: String): PathPoint = Json.decodeFromString<PathPoint>(value)
}

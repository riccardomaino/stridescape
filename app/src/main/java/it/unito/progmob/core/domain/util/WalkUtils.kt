package it.unito.progmob.core.domain.util

import android.location.Location
import it.unito.progmob.tracking.domain.model.PathPoint
import java.math.RoundingMode
import kotlin.math.roundToInt

object WalkUtils {
    /**
     * This method calculates the approximate distance in meters between two PathPoints
     *
     * @param firstPathPoint the first PathPoint
     * @param secondPathPoint the second PathPoint
     */
    fun getDistanceBetweenTwoPathPoints(
        firstPathPoint: PathPoint.LocationPoint,
        secondPathPoint: PathPoint.LocationPoint
    ): Int {
        val result =
            FloatArray(1) // Float array used to store the result from the Location.distanceBetween method
        Location.distanceBetween(
            firstPathPoint.lat,
            firstPathPoint.lng,
            secondPathPoint.lat,
            secondPathPoint.lng,
            result
        )
        return result[0].roundToInt()
    }

    /**
     * This method calculates the total calories burnt by a user based on his weight and the
     * distance covered
     *
     * @param weightInKg the weight of the user in kilograms
     * @param distanceInMeters the distance covered by the user in meters
     */
    fun getCaloriesBurnt(weightInKg: Float, distanceInMeters: Int): Int =
        ((0.75f * weightInKg) * (distanceInMeters / 1000f)).toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP).toInt()
}
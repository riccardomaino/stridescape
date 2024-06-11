package it.unito.progmob.core.domain.util

import android.location.Location
import it.unito.progmob.tracking.domain.model.PathPoint
import java.math.RoundingMode
import kotlin.math.roundToInt

object WalkUtils {

    /**
     * This method converts the speed from m/s to km/h
     *
     * @param speed the speed in m/s to convert
     */
    fun convertSpeedToKmH(speed: Float): Float = (speed * 3.6f).toBigDecimal()
        .setScale(2, RoundingMode.HALF_UP).toFloat()

    /**
     * This method converts the distance from meters to kilometers with one decimal place
     *
     * @param distanceInMeters the distance in meters to convert
     */
    fun convertMetersToKm(distanceInMeters: Int): Float = (distanceInMeters.toFloat() / 1000.toFloat()).toBigDecimal()
        .setScale(1, RoundingMode.HALF_UP).toFloat()


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
        ((0.5f * weightInKg) * (distanceInMeters / 1000f)).toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP).toInt()

    /**
     * This method calculates the average speed from a list of PathPoints
     *
     * @param pathPoints the list of PathPoints to calculate the average speed from
     */
    fun getAverageSpeedInKmH(pathPoints: List<PathPoint>): Float {
        return if (pathPoints.indexOfFirst { it is PathPoint.LocationPoint } == -1) 0f
        else pathPoints.filterIsInstance<PathPoint.LocationPoint>()
            .map { it.speed }
            .average()
            .toBigDecimal()
            .setScale(1, RoundingMode.HALF_UP)
            .toFloat()
    }

    /**
     * This method formats the distance in meters to a string expressing the distance in kilometers
     * with one decimal place
     *
     * @param distanceInMeters the distance in meters to format
     */
    fun formatDistanceToKm(distanceInMeters: Int): String =
        (distanceInMeters.toFloat() / 1000.toFloat()).toBigDecimal()
            .setScale(1, RoundingMode.HALF_UP).toString()


    fun List<PathPoint>.lastLocationPoint(): PathPoint.LocationPoint? {
        for (i in lastIndex downTo 0)
            if (get(i) is PathPoint.LocationPoint)
                return get(i) as PathPoint.LocationPoint
        return null
    }

    fun List<PathPoint>.lastEmptyPointIndex(): Int {
        return indexOfLast { it is PathPoint.EmptyPoint }
//        return if (index != -1) this[index] as PathPoint.EmptyPoint else null
//        for (i in lastIndex downTo 0)
//            if (get(i) is PathPoint.EmptyPoint)
//                return get(i) as PathPoint.EmptyPoint
//        return null
    }

    fun List<PathPoint>.firstLocationPoint() =
        find { it is PathPoint.LocationPoint } as? PathPoint.LocationPoint
}
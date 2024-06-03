package it.unito.progmob.stats.domain.usecase

import android.util.Log
import it.unito.progmob.core.domain.ext.rangeTo
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Float representing the distance walked for
 * each day in the given date range.
 */
class GetDistanceDataUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Float representing the distance walked for each day in
     * the given date range.
     *
     * @param startDate The start date in epoch milliseconds.
     * @param endDate The end date in epoch milliseconds.
     */
    operator fun invoke(startDate: Long, endDate: Long): List<Pair<LocalDate, Float>> {
        val formattedStartDate = DateUtils.formatDateFromEpochMillis(startDate)
        val formattedEndDate = DateUtils.formatDateFromEpochMillis(endDate)
        val startLocalDate = DateUtils.getLocalDateFromEpochMillis(startDate)
        val endLocalDate = DateUtils.getLocalDateFromEpochMillis(endDate)
        val dateRangeList = startLocalDate.rangeTo(endLocalDate).asIterable().toList()

        var resultPairsList = walkRepository.findDistanceForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyDistanceTuple ->
                Pair(
                    LocalDate.parse(
                        dailyDistanceTuple.date,
                        format = DateUtils.defaultFormatter
                    ),
                    WalkUtils.convertMetersToKm(dailyDistanceTuple.distance)
                )
            }
        } ?: emptyList()

        resultPairsList.toMutableList().addAll(dateRangeList.filter { date ->
            resultPairsList.none { it.first == date }
        }.map { date ->
            Pair(date, 0f)
        })

        resultPairsList = resultPairsList.sortedBy { it.first }

        resultPairsList.forEach{
            Log.d("GetDistanceDataUseCase", "date: ${it.first}, distance: ${it.second}")
        }

        return resultPairsList
    }
}
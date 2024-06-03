package it.unito.progmob.stats.domain.usecase

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
        val dateRange = startLocalDate.rangeTo(endLocalDate).asIterable()
        val pairList = mutableListOf<Pair<LocalDate, Float>>()
        var nextDate: LocalDate? =
            if (dateRange.iterator().hasNext()) dateRange.iterator().next() else null

        walkRepository.findDistanceForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyDistanceTuple ->
                if (dateRange.iterator().hasNext()) {
                    nextDate = dateRange.iterator().next()
                    while (nextDate!! < LocalDate.parse(
                            dailyDistanceTuple.date,
                            format = DateUtils.defaultFormatter
                        )
                    ) {
                        // pairList.add(Pair(nextDate, 0f))
                    }
                }
                pairList.add(
                    Pair(
                        LocalDate.parse(
                            dailyDistanceTuple.date,
                            format = DateUtils.defaultFormatter
                        ),
                        WalkUtils.convertMetersToKm(dailyDistanceTuple.distance)
                    )
                )
            }
        } ?: emptyList()
        return pairList
    }
}
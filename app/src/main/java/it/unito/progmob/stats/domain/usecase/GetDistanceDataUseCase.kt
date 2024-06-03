package it.unito.progmob.stats.domain.usecase

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
//        val startLocalDateTime = Instant.fromEpochMilliseconds(startDate)
//            .toLocalDateTime(TimeZone.currentSystemDefault())
//        val endLocalDateTime = Instant.fromEpochMilliseconds(startDate)
//            .toLocalDateTime(TimeZone.currentSystemDefault())
//        Log.d("GetDistanceDataUseCase", "Range: ${startLocalDateTime.rangeTo(endLocalDateTime)}")
        return walkRepository.findDistanceForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyDistanceTuple ->
                Pair(
                    LocalDate.parse(dailyDistanceTuple.date, format = DateUtils.defaultFormatter),
                    WalkUtils.convertMetersToKm(dailyDistanceTuple.distance)
                )
            }
        } ?: emptyList()
    }
}
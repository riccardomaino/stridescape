package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.TimeUtils
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Int representing the time in minutes
 * walked for each day in the given date range.
 */
class GetTimeDataUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Int representing the time in minutes walked for each
     * day in the given date range.
     *
     * @param startDate The start date in epoch milliseconds.
     * @param endDate The end date in epoch milliseconds.
     */
    operator fun invoke(
        startDate: Long,
        endDate: Long
    ): List<Pair<LocalDate, Int>> {
        val formattedStartDate = DateUtils.formatDateFromEpochMillis(startDate)
        val formattedEndDate = DateUtils.formatDateFromEpochMillis(endDate)
        return walkRepository.findTimeForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyTimeTuple ->
                Pair(
                    LocalDate.parse(dailyTimeTuple.date, format = DateUtils.defaultFormatter),
                    TimeUtils.convertMillisToMinutes(dailyTimeTuple.time)
                )
            }
        } ?: emptyList()
    }
}
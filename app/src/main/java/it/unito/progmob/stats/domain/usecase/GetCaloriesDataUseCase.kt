package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Int representing the calories burned for
 * each day in the given date range.
 */
class GetCaloriesDataUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Int representing the calories burned for each day in
     * the given date range.
     *
     * @param startDate The start date in epoch milliseconds.
     * @param endDate The end date in epoch milliseconds.
     */
    operator fun invoke(startDate: Long, endDate: Long): List<Pair<LocalDate, Int>> {
        val formattedStartDate = DateUtils.formatDateFromEpochMillis(startDate)
        val formattedEndDate = DateUtils.formatDateFromEpochMillis(endDate)
        return walkRepository.findCaloriesForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyCaloriesTuple ->
                Pair(
                    LocalDate.parse(dailyCaloriesTuple.date, format = DateUtils.defaultFormatter),
                    dailyCaloriesTuple.calories
                )
            }
        } ?: emptyList()
    }
}
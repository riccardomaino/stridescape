package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Float representing the distance walked for
 * each month of the current year
 */
class GetYearDistanceStatUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Float representing the distance walked for each
     * month of the current year
     */
    operator fun invoke(): List<Pair<LocalDate, Float>> {
        val currentYear = DateUtils.getCurrentYear()
        val formattedCurrentYear = DateUtils.formattedCurrentYear()
        val monthRangeList = (1..12).toMutableList()

        val resultPairsList =
            walkRepository.findDistanceForYear(formattedCurrentYear)?.let {
                it.map { monthDistanceTuple ->
                    Pair(
                        LocalDate(year = currentYear, monthNumber = monthDistanceTuple.month, dayOfMonth = 1),
                        WalkUtils.convertMetersToKm(monthDistanceTuple.distance)
                    )
                }
            }?.toMutableList() ?: mutableListOf()

        resultPairsList.addAll(monthRangeList.filter { month ->
            resultPairsList.none { it.first.monthNumber == month }
        }.map { month ->
            Pair(
                LocalDate(year = currentYear, monthNumber = month, dayOfMonth = 1),
                0f
            )
        })

        return resultPairsList.sortedBy { it.first }
    }
}
package it.unito.progmob.stats.domain.usecase

import android.util.Log
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.core.domain.util.WalkUtils
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Int representing the calories burned for
 * for each month of the current year
 */
class GetYearCaloriesStatUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Int representing the calories burned for each month
     * of the current year
     */
    operator fun invoke(): List<Pair<LocalDate, Int>> {
        val currentYear = DateUtils.getCurrentYear()
        val formattedCurrentYear = DateUtils.formattedCurrentYear()
        val monthRangeList = (1..12).toMutableList()

        val resultPairsList =
            walkRepository.findCaloriesForYear(formattedCurrentYear)?.let {
                it.map { monthCaloriesTuple ->
                    Pair(
                        LocalDate(year = currentYear, monthNumber = monthCaloriesTuple.month, dayOfMonth = 1),
                        monthCaloriesTuple.calories
                    )
                }
            }?.toMutableList() ?: mutableListOf()

        resultPairsList.addAll(monthRangeList.filter { month ->
            resultPairsList.none { it.first.monthNumber == month }
        }.map { month ->
            Pair(
                LocalDate(year = currentYear, monthNumber = month, dayOfMonth = 1),
                0
            )
        })

        return resultPairsList.sortedBy { it.first }
    }
}
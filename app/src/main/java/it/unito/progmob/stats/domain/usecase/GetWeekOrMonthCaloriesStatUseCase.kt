package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.ext.rangeTo
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.RangeType
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Int representing the calories burned for
 * each day of the current week or month
 */
class GetWeekOrMonthCaloriesStatUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Int representing the calories burned for each day of
     * the current week or month
     *
     * @param rangeType the range type to consider
     */
    operator fun invoke(rangeType: RangeType): List<Pair<LocalDate, Int>> {
        lateinit var formattedStartDate: String
        lateinit var formattedEndDate: String
        lateinit var startLocalDate: LocalDate
        lateinit var endLocalDate: LocalDate

        when(rangeType){
            RangeType.WEEK -> {
                formattedStartDate = DateUtils.formattedFirstDateOfWeek()
                formattedEndDate = DateUtils.formattedLastDateOfWeek()
                startLocalDate = DateUtils.getFirstDateOfWeek()
                endLocalDate = DateUtils.getLastDateOfWeek()
            }
            RangeType.MONTH -> {
                formattedStartDate = DateUtils.formattedFirstDateOfMonth()
                formattedEndDate = DateUtils.formattedLastDateOfMonth()
                startLocalDate = DateUtils.getFirstDateOfMonth()
                endLocalDate = DateUtils.getLastDateOfMonth()
            }

            else -> return emptyList()
        }

        val dateRangeList = startLocalDate.rangeTo(endLocalDate).asIterable().toMutableList()

        val resultPairsList = walkRepository.findCaloriesForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyCaloriesTuple ->
                Pair(
                    LocalDate.parse(dailyCaloriesTuple.date, format = DateUtils.defaultFormatter),
                    dailyCaloriesTuple.calories
                )
            }
        }?.toMutableList() ?: mutableListOf()

        resultPairsList.addAll(dateRangeList.filter { date ->
            resultPairsList.none { it.first == date }
        }.map { date ->
            Pair(date, 0)
        })

        return resultPairsList.sortedBy { it.first }
    }
}
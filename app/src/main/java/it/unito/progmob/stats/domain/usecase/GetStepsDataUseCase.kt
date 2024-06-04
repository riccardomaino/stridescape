package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.ext.rangeTo
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.RangeType
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Int representing the steps walked for
 * each day in the given date range.
 */
class GetStepsDataUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {

    /**
     * Returns a list of pairs of LocalDate and Int representing the steps walked for each day in
     * the given date range.
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

        val resultPairsList = walkRepository.findStepsForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyStepsTuple ->
                Pair(
                    LocalDate.parse(dailyStepsTuple.date, format = DateUtils.defaultFormatter),
                    dailyStepsTuple.steps
                )
            }
        } ?.toMutableList() ?: mutableListOf()

        resultPairsList.addAll(dateRangeList.filter { date ->
            resultPairsList.none { it.first == date }
        }.map { date ->
            Pair(date, 0)
        })

        return resultPairsList.sortedBy { it.first }
    }
}
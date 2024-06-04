package it.unito.progmob.stats.domain.usecase

import it.unito.progmob.core.domain.repository.WalkRepository
import javax.inject.Inject

/**
 * Use case that returns a list of pairs of LocalDate and Float representing the distance walked for
 * each day in the given date range.
 */
class GetDistanceYearUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    /**
     * Returns a list of pairs of LocalDate and Float representing the distance walked for each day in
     * the given date range.
     */
//    operator fun invoke(): List<Pair<LocalDate, Float>> {
//        val formattedStartDate = DateUtils.formattedFirstDateOfWeek()
//        val formattedEndDate = DateUtils.formattedLastDateOfWeek()
//        val startLocalDate = DateUtils.getFirstDateOfWeek()
//        val endLocalDate = DateUtils.getLastDateOfWeek()
//        val dateRangeList = startLocalDate.rangeTo(endLocalDate).asIterable().toMutableList()
//
//        val resultPairsList =
//            walkRepository.findDistanceForYear(formattedStartDate)?.let {
//                it.map { dailyDistanceTuple ->
//                    Pair(
//                        LocalDate.parse(
//                            dailyDistanceTuple.date,
//                            format = DateUtils.defaultFormatter
//                        ),
//                        WalkUtils.convertMetersToKm(dailyDistanceTuple.distance)
//                    )
//                }
//            }?.toMutableList() ?: mutableListOf()
//
//        resultPairsList.addAll(dateRangeList.filter { date ->
//            resultPairsList.none { it.first == date }
//        }.map { date ->
//            Pair(date, 0f)
//        })
//
//        return resultPairsList.sortedBy { it.first }
//    }
}
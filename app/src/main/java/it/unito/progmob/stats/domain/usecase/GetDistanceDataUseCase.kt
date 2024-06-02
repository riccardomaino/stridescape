package it.unito.progmob.stats.domain.usecase

import android.util.Log
import it.unito.progmob.core.domain.repository.WalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.core.domain.util.WalkUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class GetDistanceDataUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    operator fun invoke(
        startDate: Long,
        endDate: Long
    ): List<Pair<LocalDate, Float>> {
        val formattedStartDate = DateUtils.formatDateFromEpochMillis(startDate)
        val formattedEndDate = DateUtils.formatDateFromEpochMillis(endDate)

        val startLocalDateTime = Instant.fromEpochMilliseconds(startDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val endLocalDateTime = Instant.fromEpochMilliseconds(startDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        Log.d("GetDistanceDataUseCase", "Range: ${startLocalDateTime.rangeTo(endLocalDateTime)}")


        return walkRepository.findDistanceForDateRange(formattedStartDate, formattedEndDate)?.let {
            it.map { dailyDistanceTuple ->
                Pair(
                    LocalDate.parse(dailyDistanceTuple.date, format = DateUtils.defaultFormatter),
                    WalkUtils.convertMetersToKm(dailyDistanceTuple.distance)
                )

            }
        } ?: emptyList()

//            .mapNotNull { listOfDailyDistanceTuples ->
//                Log.d("GetDistanceDataUseCase", "mapNotNull : $listOfDailyDistanceTuples")
//                listOfDailyDistanceTuples?.map { dailyDistanceTuple ->
//                    Pair(
//                        LocalDate.parse(dailyDistanceTuple.date, format = formatter),
//                        dailyDistanceTuple.distance.toDouble()
//                    )
//                }
//            }
    }
}
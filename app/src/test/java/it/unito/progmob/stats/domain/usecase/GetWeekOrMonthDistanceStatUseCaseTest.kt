package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.stats.domain.model.RangeType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeekOrMonthDistanceStatUseCaseTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getWeekOrMonthDistanceStatUseCase: GetWeekOrMonthDistanceStatUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getWeekOrMonthDistanceStatUseCase = GetWeekOrMonthDistanceStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get week distance with week range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values.map { pair -> pair.first to WalkUtils.convertMetersToKm(pair.second.toInt()) }
        val result = getWeekOrMonthDistanceStatUseCase(RangeType.WEEK)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get week distance with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = false,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values
        val result = getWeekOrMonthDistanceStatUseCase(RangeType.WEEK)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get month distance with month range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.MONTH
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values.map { pair -> pair.first to WalkUtils.convertMetersToKm(pair.second.toInt()) }
        val result = getWeekOrMonthDistanceStatUseCase(RangeType.MONTH)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get month distance with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = false,
            rangeType = RangeType.MONTH
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values
        val result = getWeekOrMonthDistanceStatUseCase(RangeType.MONTH)
        assertThat(result).isEqualTo(actual)
    }
}
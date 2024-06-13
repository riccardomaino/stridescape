package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.stats.domain.model.RangeType
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GetWeekOrMonthStepsStatUseCaseTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getWeekOrMonthStepsStatUseCase: GetWeekOrMonthStepsStatUseCase 

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getWeekOrMonthStepsStatUseCase = GetWeekOrMonthStepsStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get week steps with week range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getWeekOrMonthStepsStatUseCase(RangeType.WEEK)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get week steps with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getWeekOrMonthStepsStatUseCase(RangeType.WEEK)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get month steps with month range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.MONTH
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getWeekOrMonthStepsStatUseCase(RangeType.MONTH)
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get month steps with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = true,
            rangeType = RangeType.MONTH
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getWeekOrMonthStepsStatUseCase(RangeType.MONTH)
        assertThat(result).isEqualTo(actual)
    }    
}
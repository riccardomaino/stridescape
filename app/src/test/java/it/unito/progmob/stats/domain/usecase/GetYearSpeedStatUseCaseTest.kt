package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.stats.domain.model.RangeType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetYearSpeedStatUseCaseTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getYearSpeedStatUseCase: GetYearSpeedStatUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getYearSpeedStatUseCase = GetYearSpeedStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get year speed with year range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values
        val result = getYearSpeedStatUseCase()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get year speed with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = false,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.FloatStatPairData

        val actual = data.values
        val result = getYearSpeedStatUseCase()
        assertThat(result).isEqualTo(actual)
    }
}
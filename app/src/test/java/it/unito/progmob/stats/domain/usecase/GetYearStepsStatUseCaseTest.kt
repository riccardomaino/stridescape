package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.stats.domain.model.RangeType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetYearStepsStatUseCaseTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getYearStepsStatUseCase: GetYearStepsStatUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getYearStepsStatUseCase = GetYearStepsStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get year steps with year range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getYearStepsStatUseCase()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get year steps with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = true,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getYearStepsStatUseCase()
        assertThat(result).isEqualTo(actual)
    }
}
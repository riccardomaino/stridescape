package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.util.TimeUtils
import it.unito.progmob.stats.domain.model.RangeType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetYearTimeStatUseCaseTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getYearTimeStatUseCase: GetYearTimeStatUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getYearTimeStatUseCase = GetYearTimeStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get year time with year range, should return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values.map { pair -> pair.first to TimeUtils.convertMillisToMinutes(pair.second.toLong()) }
        val result = getYearTimeStatUseCase()
        assertThat(result).isEqualTo(actual)
    }

    @Test
    fun `get year time with empty database, should return a list of pairs with zeros as the second element`() {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = false,
            isInt = true,
            rangeType = RangeType.YEAR
        ) as FakeWalkRepository.StatPairData.IntStatPairData

        val actual = data.values
        val result = getYearTimeStatUseCase()
        assertThat(result).isEqualTo(actual)
    }
}
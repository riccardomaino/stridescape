package it.unito.progmob.stats.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.stats.domain.model.RangeType
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWeekOrMonthCaloriesStatUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeWalkRepository: FakeWalkRepository
    private lateinit var getWeekOrMonthCaloriesStatUseCase: GetWeekOrMonthCaloriesStatUseCase

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        getWeekOrMonthCaloriesStatUseCase = GetWeekOrMonthCaloriesStatUseCase(fakeWalkRepository)
    }

    @Test
    fun `get week calories with week range, return the expected list of pairs`() {
        val data = fakeWalkRepository.addWalkEntitiesReturnPairsForTest(
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.WalkPairData.IntWalkPairData

        val actualValues = data.values
        val result = getWeekOrMonthCaloriesStatUseCase(RangeType.WEEK)
        assertThat(result).isEqualTo(actualValues)
    }

//    @Test
//    fun `get month calories with month range, return the expected list of pairs`() {
//        fakeWalkRepository.shouldHaveFilledWalkList(true)
//        val data = fakeWalkRepository.addWalkEntitiesReturnPairsForTest(isInt = true, isWeek = false)
//                as FakeWalkRepository.WalkPairData.IntWalkPairData
//        val actualValue = data.values
//        val calories = getWeekOrMonthCaloriesStatUseCase(RangeType.MONTH)
//        assertEquals(actualValue, calories)
//    }
//
//    @Test
//    fun `get week or month calories with empty database, should return 0`() {
//        fakeWalkRepository.shouldHaveFilledWalkList(false)
//        val actualValue = 0
//        val calories = getWeekOrMonthCaloriesStatUseCase(RangeType.WEEK)
//        assertEquals(actualValue, calories)
//    }

}
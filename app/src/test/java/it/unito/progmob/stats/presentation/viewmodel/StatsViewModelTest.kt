package it.unito.progmob.stats.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.util.WalkUtils
import it.unito.progmob.stats.domain.model.RangeType
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthCaloriesStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthDistanceStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthSpeedStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthStepsStatUseCase
import it.unito.progmob.stats.domain.usecase.GetWeekOrMonthTimeStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearCaloriesStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearDistanceStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearSpeedStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearStepsStatUseCase
import it.unito.progmob.stats.domain.usecase.GetYearTimeStatUseCase
import it.unito.progmob.stats.domain.usecase.StatsUseCases
import it.unito.progmob.stats.presentation.StatsEvent
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()

    private lateinit var statsViewModel: StatsViewModel
    private lateinit var statsUseCases: StatsUseCases
    private lateinit var fakeWalkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        statsUseCases = StatsUseCases(
            GetWeekOrMonthDistanceStatUseCase(fakeWalkRepository),
            GetWeekOrMonthTimeStatUseCase(fakeWalkRepository),
            GetWeekOrMonthCaloriesStatUseCase(fakeWalkRepository),
            GetWeekOrMonthStepsStatUseCase(fakeWalkRepository),
            GetWeekOrMonthSpeedStatUseCase(fakeWalkRepository),
            GetYearDistanceStatUseCase(fakeWalkRepository),
            GetYearTimeStatUseCase(fakeWalkRepository),
            GetYearCaloriesStatUseCase(fakeWalkRepository),
            GetYearStepsStatUseCase(fakeWalkRepository),
            GetYearSpeedStatUseCase(fakeWalkRepository)
        )
        statsViewModel = StatsViewModel(statsUseCases)
    }

    @Test
    fun `test event stats type selected with range week, should update the steps field of the ui stats state`() = runTest {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStatsSelected = StatsType.STEPS
        val actualStepsChartValues = data.values

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(StatsType.STEPS))
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the distance field of the ui stats state`() {
        val data = fakeWalkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.FloatStatPairData
        val actualStatsSelected = StatsType.DISTANCE
        val actualDistanceChartValues = data.values.map { pair -> pair.first to WalkUtils.convertMetersToKm(pair.second.toInt()) }

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(StatsType.DISTANCE))
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val distanceChartValues = statsViewModel.uiStatsState.value.distanceChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(distanceChartValues).isEqualTo(actualDistanceChartValues)
    }

}
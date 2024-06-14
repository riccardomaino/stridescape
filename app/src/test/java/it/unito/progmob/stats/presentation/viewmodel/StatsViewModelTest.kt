package it.unito.progmob.stats.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.util.TimeUtils
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
    private lateinit var walkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        walkRepository = FakeWalkRepository()
        statsUseCases = StatsUseCases(
            GetWeekOrMonthDistanceStatUseCase(walkRepository),
            GetWeekOrMonthTimeStatUseCase(walkRepository),
            GetWeekOrMonthCaloriesStatUseCase(walkRepository),
            GetWeekOrMonthStepsStatUseCase(walkRepository),
            GetWeekOrMonthSpeedStatUseCase(walkRepository),
            GetYearDistanceStatUseCase(walkRepository),
            GetYearTimeStatUseCase(walkRepository),
            GetYearCaloriesStatUseCase(walkRepository),
            GetYearStepsStatUseCase(walkRepository),
            GetYearSpeedStatUseCase(walkRepository)
        )
        statsViewModel = StatsViewModel(statsUseCases)
    }

    @Test
    fun `test initialization, should update the ui stats state`() = runTest {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStatsSelected = StatsType.STEPS
        val actualStepsChartValues = data.values

        statsViewModel = StatsViewModel(statsUseCases)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the steps field of the ui stats state`() = runTest {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStatsSelected = StatsType.STEPS
        val actualStepsChartValues = data.values

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(actualStatsSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the distance field of the ui stats state`() {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.FloatStatPairData
        val actualStatsSelected = StatsType.DISTANCE
        val actualDistanceChartValues = data.values.map { pair -> pair.first to WalkUtils.convertMetersToKm(pair.second.toInt()) }

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(actualStatsSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val distanceChartValues = statsViewModel.uiStatsState.value.distanceChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(distanceChartValues).isEqualTo(actualDistanceChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the calories field of the ui stats state`() {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStatsSelected = StatsType.CALORIES
        val actualCaloriesChartValues = data.values

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(actualStatsSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val caloriesChartValues = statsViewModel.uiStatsState.value.caloriesChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(caloriesChartValues).isEqualTo(actualCaloriesChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the time field of the ui stats state`() {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStatsSelected = StatsType.TIME
        val actualTimeChartValues = data.values.map { pair -> pair.first to TimeUtils.convertMillisToMinutes(pair.second.toLong()) }

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(actualStatsSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val timeChartValues = statsViewModel.uiStatsState.value.timeChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(timeChartValues).isEqualTo(actualTimeChartValues)
    }

    @Test
    fun `test event stats type selected with range week, should update the speed field of the ui stats state`() {
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = false,
            rangeType = RangeType.WEEK
        ) as FakeWalkRepository.StatPairData.FloatStatPairData
        val actualStatsSelected = StatsType.SPEED
        val actualSpeedChartValues = data.values

        statsViewModel.onEvent(StatsEvent.StatsTypeSelected(actualStatsSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val statsSelected = statsViewModel.uiStatsState.value.statsSelected
        val speedChartValues = statsViewModel.uiStatsState.value.speedChartValues
        assertThat(statsSelected).isEqualTo(actualStatsSelected)
        assertThat(speedChartValues).isEqualTo(actualSpeedChartValues)
    }

    @Test
    fun `test event range type selected, should update the steps field of the ui stats state considering the week range`() {
        val actualRangeSelected = RangeType.WEEK
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = actualRangeSelected
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStepsChartValues = data.values

        statsViewModel.onEvent(StatsEvent.RangeTypeSelected(actualRangeSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val rangeSelected = statsViewModel.uiStatsState.value.rangeSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(rangeSelected).isEqualTo(actualRangeSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

    @Test
    fun `test event range type selected, should update the steps field of the ui stats state considering the month range`() {
        val actualRangeSelected = RangeType.MONTH
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = actualRangeSelected
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStepsChartValues = data.values

        statsViewModel.onEvent(StatsEvent.RangeTypeSelected(actualRangeSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val rangeSelected = statsViewModel.uiStatsState.value.rangeSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(rangeSelected).isEqualTo(actualRangeSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

    @Test
    fun `test event range type selected, should update the steps field of the ui stats state considering the year range`() {
        val actualRangeSelected = RangeType.YEAR
        val data = walkRepository.addWalkEntitiesForStatsTest(
            fill = true,
            isInt = true,
            rangeType = actualRangeSelected
        ) as FakeWalkRepository.StatPairData.IntStatPairData
        val actualStepsChartValues = data.values

        statsViewModel.onEvent(StatsEvent.RangeTypeSelected(actualRangeSelected))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val rangeSelected = statsViewModel.uiStatsState.value.rangeSelected
        val stepsChartValues = statsViewModel.uiStatsState.value.stepsChartValues
        assertThat(rangeSelected).isEqualTo(actualRangeSelected)
        assertThat(stepsChartValues).isEqualTo(actualStepsChartValues)
    }

}
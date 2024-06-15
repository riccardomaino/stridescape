package it.unito.progmob.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.usecase.GetTargetUseCase
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.home.domain.usecase.GetDayCaloriesUseCase
import it.unito.progmob.home.domain.usecase.GetDayDistanceUseCase
import it.unito.progmob.home.domain.usecase.GetDayStepsUseCase
import it.unito.progmob.home.domain.usecase.GetDayTimeUseCase
import it.unito.progmob.home.domain.usecase.GetWeeklyStepsUseCase
import it.unito.progmob.home.domain.usecase.GetWeeklyTargetUseCase
import it.unito.progmob.home.domain.usecase.HomeUseCases
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeUseCases: HomeUseCases
    private lateinit var walkRepository: FakeWalkRepository
    private lateinit var data: FakeWalkRepository.WalkData.IntWalkData
    private lateinit var targetRepository: FakeTargetRepository

    @Before
    fun setUp() {
        walkRepository = FakeWalkRepository()
        data = walkRepository.addWeekWalkEntitiesForTest(
            fill = true,
            isInt = true
        ) as FakeWalkRepository.WalkData.IntWalkData
        targetRepository = FakeTargetRepository()
        targetRepository.addWeeklyTargetForTest()
        homeUseCases = HomeUseCases(
            GetDayStepsUseCase(walkRepository),
            GetDayCaloriesUseCase(walkRepository),
            GetDayDistanceUseCase(walkRepository),
            GetDayTimeUseCase(walkRepository),
            GetTargetUseCase(targetRepository),
            GetWeeklyStepsUseCase(walkRepository),
            GetWeeklyTargetUseCase(targetRepository)
        )
        homeViewModel = HomeViewModel(homeUseCases)

    }

    @Test
    fun `test current day of week initialization, should return the current day`() = runTest {
        val actualCurrentDayOfWeek = DateUtils.getCurrentDayOfWeek()
        val currentDayOfWeek = homeViewModel.currentDayOfWeek.value
        assertThat(currentDayOfWeek).isEqualTo(actualCurrentDayOfWeek)
    }


    @Test
    fun `test current day steps initialization, should return the current day steps`() = runTest {
        val actualStepsCurrentDay = data.values[DateUtils.getCurrentDayOfWeek()]
        val results = mutableListOf<Int>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.stepsCurrentDay.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(0)
        assertThat(results.last()).isEqualTo(actualStepsCurrentDay)
    }

    @Test
    fun `test current day calories initialization, should return the current day calories`() = runTest {
        val actualCaloriesCurrentDay = data.values[DateUtils.getCurrentDayOfWeek()]
        val results = mutableListOf<Int>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.caloriesCurrentDay.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(0)
        assertThat(results.last()).isEqualTo(actualCaloriesCurrentDay)
    }

    @Test
    fun `test current day distance initialization, should return the current day distance`() = runTest {
        val actualDistanceCurrentDay = data.values[DateUtils.getCurrentDayOfWeek()]
        val results = mutableListOf<Int>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.distanceCurrentDay.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(0)
        assertThat(results.last()).isEqualTo(actualDistanceCurrentDay)
    }

    @Test
    fun `test current day time initialization, should return the current day time`() = runTest {
        val actualTimeCurrentDay = data.values[DateUtils.getCurrentDayOfWeek()].toLong()
        val results = mutableListOf<Long>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.timeCurrentDay.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(0)
        assertThat(results.last()).isEqualTo(actualTimeCurrentDay)
    }

    @Test
    fun `test current day step target initialization, should return the current day step target`() = runTest {
        val actualStepTargetCurrentDay = data.values[DateUtils.getCurrentDayOfWeek()]
        val results = mutableListOf<Int>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.stepsTargetCurrentDay.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(1)
        assertThat(results.last()).isEqualTo(actualStepTargetCurrentDay)
    }

    @Test
    fun `test weekly steps initialization, should return the weekly steps`() = runTest {
        val actualWeeklySteps = data.values
        val results = mutableListOf<IntArray>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.weeklySteps.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(intArrayOf(0, 0, 0, 0, 0, 0, 0))
        assertThat(results.last()).isEqualTo(actualWeeklySteps)
    }

    @Test
    fun `test weekly steps target initialization, should return the weekly steps target`() = runTest {
        val actualWeeklyStepsTarget = data.values.map {
            if(it == 0) 1 else it
        }.toIntArray()
        val results = mutableListOf<IntArray>()

        backgroundScope.launch(mainDispatcherRule.dispatcher) {
            homeViewModel.weeklyTarget.toList(results)
        }

        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        assertThat(results.size).isEqualTo(2)
        assertThat(results.first()).isEqualTo(intArrayOf(1, 1, 1, 1, 1, 1, 1))
        assertThat(results.last()).isEqualTo(actualWeeklyStepsTarget)
    }
}
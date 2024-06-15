package it.unito.progmob.history.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.usecase.GetWalksWithPathPointsUseCase
import it.unito.progmob.history.domain.usecase.HistoryUseCases
import it.unito.progmob.history.presentation.HistoryEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyUseCases: HistoryUseCases
    private lateinit var targetRepository: FakeTargetRepository
    private lateinit var walkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        targetRepository = FakeTargetRepository()
        walkRepository = FakeWalkRepository()
        historyUseCases = HistoryUseCases(
            GetWalksWithPathPointsUseCase(walkRepository)
        )
        historyViewModel = HistoryViewModel(historyUseCases)
    }

    @Test
    fun `test initialization, should update all grouped walks state`() = runTest {
        val actual = walkRepository.addWalkWithPathPointsForTest()

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val allGroupedWalks = historyViewModel.allGroupedWalks.value
        assertThat(allGroupedWalks).isEqualTo(actual)
    }

    @Test
    fun `test date range selected, should update all grouped walks state`() = runTest {
        val startDate = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds()
        val endDate = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds()
        val actual = walkRepository.addWalkWithPathPointsForTest().filter { it.date == DateUtils.formatDateFromEpochMillis(startDate) }

        historyViewModel.onEvent(HistoryEvent.DateRangeSelected(startDate, endDate))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val allGroupedWalks = historyViewModel.allGroupedWalks.value
        assertThat(allGroupedWalks).isEqualTo(actual)
    }
}
package it.unito.progmob.main.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.main.domain.usecase.CheckTargetExistUseCase
import it.unito.progmob.main.domain.usecase.MainUseCases
import it.unito.progmob.main.domain.usecase.ReadOnboardingEntryUseCase
import it.unito.progmob.main.presentation.MainEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainUseCases: MainUseCases
    private lateinit var targetRepository: FakeTargetRepository
    private lateinit var dataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        targetRepository = FakeTargetRepository()
        dataStoreManager = FakeDataStoreManager()
        mainUseCases = MainUseCases(
            ReadOnboardingEntryUseCase(dataStoreManager),
            CheckTargetExistUseCase(targetRepository)
        )
        mainViewModel = MainViewModel(mainUseCases)
        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `test initialization, should initialize correctly the start destination and target`() = runTest {
        val actualStartDestination = Route.OnBoardingNavigationRoute.route

        val isActionButtonShown = mainViewModel.isActionButtonShown.value
        val startDestination = mainViewModel.startDestination
        val isReady = mainViewModel.isReady.value

        assertThat(isActionButtonShown).isTrue()
        assertThat(startDestination).isEqualTo(actualStartDestination)
        assertThat(isReady).isTrue()
    }

    @Test
    fun `test event permission result, should add the permission to the visiblePermissionDialogQueue`() = runTest {
        val actualPermission = "FakePermission"
        val actualVisiblePermissionDialogQueue = listOf(actualPermission)

        mainViewModel.onEvent(MainEvent.PermissionResult(actualPermission, false))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val resultVisiblePermissionDialogQueue = mainViewModel.visiblePermissionDialogQueue.value
        assertThat(resultVisiblePermissionDialogQueue).isEqualTo(actualVisiblePermissionDialogQueue)
    }

    @Test
    fun `test event permission result, should not add the permission to the visiblePermissionDialogQueue`() = runTest {
        val actualPermission = "FakePermission"
        val actualVisiblePermissionDialogQueue = emptyList<String>()

        mainViewModel.onEvent(MainEvent.PermissionResult(actualPermission, true))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val resultVisiblePermissionDialogQueue = mainViewModel.visiblePermissionDialogQueue.value
        assertThat(resultVisiblePermissionDialogQueue).isEqualTo(actualVisiblePermissionDialogQueue)
    }

    @Test
    fun `test event dismiss permission dialog, should remove the permission from the visiblePermissionDialogQueue`() = runTest {
        val actualPermission = "FakePermission"
        val actualVisiblePermissionDialogQueue = emptyList<String>()

        mainViewModel.onEvent(MainEvent.PermissionResult(actualPermission, false))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        mainViewModel.onEvent(MainEvent.DismissPermissionDialog)
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val resultVisiblePermissionDialogQueue = mainViewModel.visiblePermissionDialogQueue.value
        assertThat(resultVisiblePermissionDialogQueue).isEqualTo(actualVisiblePermissionDialogQueue)
    }

    @Test
    fun `test event show floating action button, should update the showFloatingActionButton to true`() = runTest {
        val actual = true

        mainViewModel.onEvent(MainEvent.ShowFloatingActionButton(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val isActionButtonShown = mainViewModel.isActionButtonShown.value
        assertThat(isActionButtonShown).isEqualTo(actual)
    }

    @Test
    fun `test event show floating action button, should update the showFloatingActionButton to false`() = runTest {
        val actual = false

        mainViewModel.onEvent(MainEvent.ShowFloatingActionButton(true))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        mainViewModel.onEvent(MainEvent.ShowFloatingActionButton(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val isActionButtonShown = mainViewModel.isActionButtonShown.value
        assertThat(isActionButtonShown).isEqualTo(actual)
    }
}
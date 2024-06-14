package it.unito.progmob.onboarding.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.UpdateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateHeightUseCase
import it.unito.progmob.core.domain.usecase.ValidateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateUsernameUseCase
import it.unito.progmob.core.domain.usecase.ValidateWeightUseCase
import it.unito.progmob.onboarding.domain.usecase.OnBoardingUseCases
import it.unito.progmob.onboarding.domain.usecase.SaveOnboardingEntryUseCase
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OnBoardingViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()
    
    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private lateinit var onBoardingUseCases: OnBoardingUseCases
    private lateinit var targetRepository: FakeTargetRepository
    private lateinit var dataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        targetRepository = FakeTargetRepository()
        targetRepository.addCurrentDayTargetForTest(stepsTarget = 9000)
        dataStoreManager = FakeDataStoreManager()
        onBoardingUseCases = OnBoardingUseCases(
            SaveOnboardingEntryUseCase(dataStoreManager),
            SaveUsernameEntryUseCase(dataStoreManager),
            SaveUserWeightEntryUseCase(dataStoreManager),
            SaveUserHeightEntryUseCase(dataStoreManager),
            UpdateTargetUseCase(targetRepository),
            ValidateHeightUseCase(),
            ValidateTargetUseCase(),
            ValidateUsernameUseCase(),
            ValidateWeightUseCase()
        )
        onBoardingViewModel = OnBoardingViewModel(onBoardingUseCases)
    }

    @Test
    fun `test event height changed with correct input, onboarding state height should be updated`() = runTest {
        val actual = "180"

        onBoardingViewModel.onEvent(OnBoardingEvent.HeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.height).isEqualTo(actual)
        assertThat(onBoardingState.heightError).isNull()
    }

    @Test
    fun `test event height changed with wrong input, onboarding state height should be updated`() = runTest {
        val actual = "123abc"

        onBoardingViewModel.onEvent(OnBoardingEvent.HeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.height).isEqualTo("123abc")
        assertThat(onBoardingState.heightError).isNotNull()
    }

    @Test
    fun `test event target changed with correct input, onboarding state target should be updated`() = runTest {
        val actual = "3000"

        onBoardingViewModel.onEvent(OnBoardingEvent.TargetChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.target).isEqualTo(actual)
        assertThat(onBoardingState.targetError).isNull()
    }

    @Test
    fun `test event target changed with wrong input, onboarding state target should be updated`() = runTest {
        val actual = "23.1"

        onBoardingViewModel.onEvent(OnBoardingEvent.TargetChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.target).isEqualTo(actual)
        assertThat(onBoardingState.targetError).isNotNull()
    }

    @Test
    fun `test event username changed with correct input, onboarding state username should be updated`() = runTest {
        val actual = "Luigi Bianchi"

        onBoardingViewModel.onEvent(OnBoardingEvent.UsernameChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.username).isEqualTo(actual)
        assertThat(onBoardingState.usernameError).isNull()
    }

    @Test
    fun `test event username changed with wrong input, onboarding state username should be updated`() = runTest {
        val actual = ""

        onBoardingViewModel.onEvent(OnBoardingEvent.UsernameChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.username).isEqualTo(actual)
        assertThat(onBoardingState.usernameError).isNotNull()
    }

    @Test
    fun `test event weight changed with correct input, onboarding state weight should be updated`() = runTest {
        val actual = "90"

        onBoardingViewModel.onEvent(OnBoardingEvent.WeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.weight).isEqualTo(actual)
        assertThat(onBoardingState.weightError).isNull()
    }

    @Test
    fun `test event weight changed with wrong input, onboarding state weight should be updated`() = runTest {
        val actual = "500"

        onBoardingViewModel.onEvent(OnBoardingEvent.WeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.weight).isEqualTo(actual)
        assertThat(onBoardingState.weightError).isNotNull()
    }

    @Test
    fun `test event save profile with correct input, onboarding state should be updated and onboarding use cases should be called to save data`() = runTest {
        val actualUsername = "Luigi Bianchi"
        val actualHeight = "160"
        val actualWeight = "75"
        val actualTarget = "9999"

        onBoardingViewModel.onEvent(OnBoardingEvent.UsernameChanged(actualUsername))
        onBoardingViewModel.onEvent(OnBoardingEvent.HeightChanged(actualHeight))
        onBoardingViewModel.onEvent(OnBoardingEvent.WeightChanged(actualWeight))
        onBoardingViewModel.onEvent(OnBoardingEvent.TargetChanged(actualTarget))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        onBoardingViewModel.onEvent(OnBoardingEvent.SaveProfile)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.username).isEqualTo(actualUsername)
        assertThat(onBoardingState.usernameError).isNull()
        assertThat(onBoardingState.height).isEqualTo(actualHeight)
        assertThat(onBoardingState.heightError).isNull()
        assertThat(onBoardingState.weight).isEqualTo(actualWeight)
        assertThat(onBoardingState.weightError).isNull()
        assertThat(onBoardingState.target).isEqualTo(actualTarget)
        assertThat(onBoardingState.targetError).isNull()
    }

    @Test
    fun `test event save profile with wrong input, onboarding state should be updated and onboarding use cases should be called to save data`() = runTest {
        val actualUsername = ""
        val actualHeight = "160abc"
        val actualWeight = "1231"
        val actualTarget = "0"

        onBoardingViewModel.onEvent(OnBoardingEvent.UsernameChanged(actualUsername))
        onBoardingViewModel.onEvent(OnBoardingEvent.HeightChanged(actualHeight))
        onBoardingViewModel.onEvent(OnBoardingEvent.WeightChanged(actualWeight))
        onBoardingViewModel.onEvent(OnBoardingEvent.TargetChanged(actualTarget))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        onBoardingViewModel.onEvent(OnBoardingEvent.SaveProfile)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val onBoardingState = onBoardingViewModel.onBoardingState.value
        assertThat(onBoardingState.username).isEqualTo(actualUsername)
        assertThat(onBoardingState.usernameError).isNotNull()
        assertThat(onBoardingState.height).isEqualTo(actualHeight)
        assertThat(onBoardingState.heightError).isNotNull()
        assertThat(onBoardingState.weight).isEqualTo(actualWeight)
        assertThat(onBoardingState.weightError).isNotNull()
        assertThat(onBoardingState.target).isEqualTo(actualTarget)
        assertThat(onBoardingState.targetError).isNotNull()
    }
    
}
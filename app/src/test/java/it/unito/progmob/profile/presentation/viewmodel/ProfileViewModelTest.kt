package it.unito.progmob.profile.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.core.data.manager.FakeDataStoreManager
import it.unito.progmob.core.data.repository.FakeTargetRepository
import it.unito.progmob.core.domain.usecase.GetTargetUseCase
import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.UpdateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateHeightUseCase
import it.unito.progmob.core.domain.usecase.ValidateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateUsernameUseCase
import it.unito.progmob.core.domain.usecase.ValidateWeightUseCase
import it.unito.progmob.profile.domain.usecase.ProfileUseCases
import it.unito.progmob.profile.presentation.ProfileEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ProfileViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileUseCases: ProfileUseCases
    private lateinit var targetRepository: FakeTargetRepository
    private lateinit var dataStoreManager: FakeDataStoreManager

    @Before
    fun setUp() {
        targetRepository = FakeTargetRepository()
        targetRepository.addCurrentDayTargetForTest(stepsTarget = 9000)
        dataStoreManager = FakeDataStoreManager()
        profileUseCases = ProfileUseCases(
            ValidateUsernameUseCase(),
            ValidateHeightUseCase(),
            ValidateWeightUseCase(),
            ValidateTargetUseCase(),
            SaveUserHeightEntryUseCase(dataStoreManager),
            SaveUserWeightEntryUseCase(dataStoreManager),
            SaveUsernameEntryUseCase(dataStoreManager),
            UpdateTargetUseCase(targetRepository),
            ReadUsernameEntryUseCase(dataStoreManager),
            ReadUserHeightEntryUseCase(dataStoreManager),
            ReadUserWeightEntryUseCase(dataStoreManager),
            GetTargetUseCase(targetRepository)
        )
        profileViewModel = ProfileViewModel(profileUseCases)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `test event height changed with correct input, profile state height should be updated`() = runTest {
        val actual = "180"

        profileViewModel.onEvent(ProfileEvent.HeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.height).isEqualTo(actual)
        assertThat(profileState.heightError).isNull()
    }

    @Test
    fun `test event height changed with wrong input, profile state height should be updated`() = runTest {
        val actual = "123abc"

        profileViewModel.onEvent(ProfileEvent.HeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.height).isEqualTo("123abc")
        assertThat(profileState.heightError).isNotNull()
    }

    @Test
    fun `test event target changed with correct input, profile state target should be updated`() = runTest {
        val actual = "3000"

        profileViewModel.onEvent(ProfileEvent.TargetChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.target).isEqualTo(actual)
        assertThat(profileState.targetError).isNull()
    }

    @Test
    fun `test event target changed with wrong input, profile state target should be updated`() = runTest {
        val actual = "23.1"

        profileViewModel.onEvent(ProfileEvent.TargetChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.target).isEqualTo(actual)
        assertThat(profileState.targetError).isNotNull()
    }

    @Test
    fun `test event username changed with correct input, profile state username should be updated`() = runTest {
        val actual = "Luigi Bianchi"

        profileViewModel.onEvent(ProfileEvent.UsernameChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.username).isEqualTo(actual)
        assertThat(profileState.usernameError).isNull()
    }

    @Test
    fun `test event username changed with wrong input, profile state username should be updated`() = runTest {
        val actual = ""

        profileViewModel.onEvent(ProfileEvent.UsernameChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.username).isEqualTo(actual)
        assertThat(profileState.usernameError).isNotNull()
    }

    @Test
    fun `test event weight changed with correct input, profile state weight should be updated`() = runTest {
        val actual = "90"

        profileViewModel.onEvent(ProfileEvent.WeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.weight).isEqualTo(actual)
        assertThat(profileState.weightError).isNull()
    }

    @Test
    fun `test event weight changed with wrong input, profile state weight should be updated`() = runTest {
        val actual = "500"

        profileViewModel.onEvent(ProfileEvent.WeightChanged(actual))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.weight).isEqualTo(actual)
        assertThat(profileState.weightError).isNotNull()
    }

    @Test
    fun `test event save profile with correct input, profile state should be updated and profile use cases should be called to save data`() = runTest {
        val actualUsername = "Luigi Bianchi"
        val actualHeight = "160"
        val actualWeight = "75"
        val actualTarget = "9999"

        profileViewModel.onEvent(ProfileEvent.UsernameChanged(actualUsername))
        profileViewModel.onEvent(ProfileEvent.HeightChanged(actualHeight))
        profileViewModel.onEvent(ProfileEvent.WeightChanged(actualWeight))
        profileViewModel.onEvent(ProfileEvent.TargetChanged(actualTarget))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        profileViewModel.onEvent(ProfileEvent.SaveProfile)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.username).isEqualTo(actualUsername)
        assertThat(profileState.usernameError).isNull()
        assertThat(profileState.height).isEqualTo(actualHeight)
        assertThat(profileState.heightError).isNull()
        assertThat(profileState.weight).isEqualTo(actualWeight)
        assertThat(profileState.weightError).isNull()
        assertThat(profileState.target).isEqualTo(actualTarget)
        assertThat(profileState.targetError).isNull()
    }

    @Test
    fun `test event save profile with wrong input, profile state should be updated and profile use cases should be called to save data`() = runTest {
        val actualUsername = ""
        val actualHeight = "160abc"
        val actualWeight = "1231"
        val actualTarget = "0"

        profileViewModel.onEvent(ProfileEvent.UsernameChanged(actualUsername))
        profileViewModel.onEvent(ProfileEvent.HeightChanged(actualHeight))
        profileViewModel.onEvent(ProfileEvent.WeightChanged(actualWeight))
        profileViewModel.onEvent(ProfileEvent.TargetChanged(actualTarget))
        othersDispatcherRule.defaultDispatcher.scheduler.advanceUntilIdle()

        profileViewModel.onEvent(ProfileEvent.SaveProfile)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val profileState = profileViewModel.profileState.value
        assertThat(profileState.username).isEqualTo(actualUsername)
        assertThat(profileState.usernameError).isNotNull()
        assertThat(profileState.height).isEqualTo(actualHeight)
        assertThat(profileState.heightError).isNotNull()
        assertThat(profileState.weight).isEqualTo(actualWeight)
        assertThat(profileState.weightError).isNotNull()
        assertThat(profileState.target).isEqualTo(actualTarget)
        assertThat(profileState.targetError).isNotNull()
    }
}
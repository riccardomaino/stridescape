package it.unito.progmob.tracking.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.unito.progmob.MainDispatcherRule
import it.unito.progmob.OthersDispatcherRule
import it.unito.progmob.tracking.domain.manager.LocationTrackingManager
import it.unito.progmob.tracking.domain.service.WalkHandler
import it.unito.progmob.tracking.domain.usecase.TrackingUseCases
import it.unito.progmob.tracking.presentation.TrackingEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class TrackingViewModelTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACTIVITY_RECOGNITION,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var othersDispatcherRule = OthersDispatcherRule()

    private lateinit var trackingViewModel: TrackingViewModel

    @Inject
    lateinit var trackingUseCases: TrackingUseCases
    @Inject
    lateinit var walkHandler: WalkHandler
    @Inject
    lateinit var locationTrackingManager: LocationTrackingManager

    @Before
    fun setUp() {
        hiltRule.inject()
        trackingViewModel = TrackingViewModel(trackingUseCases, walkHandler, locationTrackingManager)
    }

    @Test
    fun testInitialization_locationEnabledStateUpdated() = runTest {
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.isLocationEnabled.value
        assertThat(result).isTrue()
    }

    @Test
    fun testInitialization_trackSingleLocation() = runTest {
        val actual = LatLng(1.0, 1.0)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.lastKnownLocation.value
        assertThat(result?.latitude).isEqualTo(actual.latitude)
        assertThat(result?.longitude).isEqualTo(actual.longitude)
    }

    @Test
    fun test_startTrackingService(){
        trackingViewModel.onEvent(TrackingEvent.StartTrackingService)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.uiTrackingState.value.isTracking
        assertThat(result).isTrue()
    }

    @Test
    fun test_pauseTrackingService(){
        trackingViewModel.onEvent(TrackingEvent.PauseTrackingService)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.uiTrackingState.value.isTracking
        assertThat(result).isFalse()
    }

    @Test
    fun test_stopTrackingService(){
        trackingViewModel.onEvent(TrackingEvent.StopTrackingService)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.uiTrackingState.value.isTracking
        assertThat(result).isFalse()
    }

    @Test
    fun test_resumeTrackingService(){
        trackingViewModel.onEvent(TrackingEvent.StartTrackingService)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        trackingViewModel.onEvent(TrackingEvent.StopTrackingService)
        trackingViewModel.onEvent(TrackingEvent.ResumeTrackingService)
        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()

        val result = trackingViewModel.uiTrackingState.value.isTracking
        assertThat(result).isTrue()
    }

    @Test
    fun test_trackSingleLocation(){
        val actual = LatLng(1.0, 1.0)
        trackingViewModel.onEvent(TrackingEvent.TrackSingleLocation)

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.lastKnownLocation.value
        assertThat(result?.latitude).isEqualTo(actual.latitude)
        assertThat(result?.longitude).isEqualTo(actual.longitude)
    }

    @Test
    fun test_showStopWalkDialogTrue(){
        trackingViewModel.onEvent(TrackingEvent.ShowStopWalkDialog(true))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.showStopWalkDialog.value
        assertThat(result).isTrue()
    }

    @Test
    fun test_showStopWalkDialogFalse(){
        trackingViewModel.onEvent(TrackingEvent.ShowStopWalkDialog(false))

        othersDispatcherRule.ioDispatcher.scheduler.advanceUntilIdle()
        mainDispatcherRule.dispatcher.scheduler.advanceUntilIdle()
        val result = trackingViewModel.showStopWalkDialog.value
        assertThat(result).isFalse()
    }
}
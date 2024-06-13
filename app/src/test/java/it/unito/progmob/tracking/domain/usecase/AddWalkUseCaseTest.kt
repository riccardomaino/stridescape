package it.unito.progmob.tracking.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import it.unito.progmob.core.data.repository.FakeWalkRepository
import it.unito.progmob.tracking.domain.model.PathPoint
import it.unito.progmob.tracking.presentation.state.UiTrackingState
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddWalkUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addWalkUseCase: AddWalkUseCase
    private lateinit var fakeWalkRepository: FakeWalkRepository

    @Before
    fun setUp() {
        fakeWalkRepository = FakeWalkRepository()
        addWalkUseCase = AddWalkUseCase(fakeWalkRepository)
    }

    @Test
    fun `add new walk, should not return null`() = runTest {
        fakeWalkRepository.shouldHaveFilledWalkList(false)
        val pathPoints = emptyList<PathPoint>()
        val newWalk = UiTrackingState(isTrackingStarted = false,  isTracking = false,1000, 320000, 2f, pathPoints, 1000, 1000)
        val newWalkId = addWalkUseCase(newWalk)
        val addedWalk = fakeWalkRepository.findWalkById(newWalkId)
        assertThat(addedWalk).isNotNull()
    }
}
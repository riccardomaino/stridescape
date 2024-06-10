package it.unito.progmob.profile.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unito.progmob.profile.domain.usecase.ProfileUseCases
import it.unito.progmob.profile.presentation.ProfileEvent
import it.unito.progmob.profile.presentation.state.UiProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    var profileState = mutableStateOf(UiProfileState())
        private set

    private lateinit var initialUsername: String
    private lateinit var initialHeight: String
    private lateinit var initialWeight: String
    private lateinit var initialTarget: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                profileUseCases.readUserHeightEntryUseCase().take(1).collect { height ->
                    profileState.value.height = height
                    initialHeight = height
                }
            }
            launch {
                profileUseCases.readUserWeightEntryUseCase().take(1).collect { weight ->
                    profileState.value.weight = weight
                    initialWeight = weight
                }
            }
            launch {
                profileUseCases.readUsernameEntryUseCase().take(1).collect { username ->
                    profileState.value.username = username
                    initialUsername = username
                }
            }
            launch {
                profileUseCases.getTargetUseCase().take(1).collect { target ->
                    profileState.value.target = target.toString()
                    initialTarget = target.toString()
                }
            }
        }
    }

    /**
     * Handles Profile events emitted from the UI.
     * @param event The ProfileEvent to be processed.
     */
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.HeightChanged -> validateHeight(event.height)
            is ProfileEvent.TargetChanged -> validateTarget(event.target)
            is ProfileEvent.UsernameChanged -> validateUsername(event.username)
            is ProfileEvent.WeightChanged -> validateWeight(event.weight)
            is ProfileEvent.SaveProfile -> saveProfile()
        }
    }

    private fun saveProfile() {
        viewModelScope.launch(Dispatchers.Default) {
            if (profileState.value.usernameError == null &&
                profileState.value.heightError == null &&
                profileState.value.weightError == null &&
                profileState.value.targetError == null
            ) {
                if(initialUsername != profileState.value.username){
                    launch { profileUseCases.saveUsernameEntryUseCase(profileState.value.username) }
                }
                if(initialHeight != profileState.value.height){
                    launch { profileUseCases.saveUserHeightEntryUseCase(profileState.value.height) }
                }
                if(initialWeight != profileState.value.weight){
                    launch { profileUseCases.saveUserWeightEntryUseCase(profileState.value.weight) }
                }
                if(initialTarget != profileState.value.target){
                    launch { profileUseCases.updateTargetUseCase(profileState.value.target) }
                }
            }
        }
    }

    private fun validateWeight(weight: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = profileUseCases.validateWeightUseCase(weight)
            profileState.value = profileState.value.copy(
                weight = weight,
                weightError = validationResult.message
            )
        }
    }

    private fun validateUsername(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = profileUseCases.validateUsernameUseCase(username)
            profileState.value = profileState.value.copy(
                username = username,
                usernameError = validationResult.message
            )
        }
    }

    private fun validateTarget(target: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = profileUseCases.validateTargetUseCase(target)
            profileState.value = profileState.value.copy(
                target = target,
                targetError = validationResult.message
            )
        }
    }

    private fun validateHeight(height: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val validationResult = profileUseCases.validateHeightUseCase(height)
            profileState.value = profileState.value.copy(
                height = height,
                heightError = validationResult.message
            )
        }
    }
}
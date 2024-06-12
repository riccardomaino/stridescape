package it.unito.progmob.profile.domain.usecase

import it.unito.progmob.core.domain.usecase.GetTargetUseCase
import it.unito.progmob.core.domain.usecase.ReadUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.ReadUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserHeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUserWeightEntryUseCase
import it.unito.progmob.core.domain.usecase.SaveUsernameEntryUseCase
import it.unito.progmob.core.domain.usecase.UpdateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateHeightUseCase
import it.unito.progmob.core.domain.usecase.ValidateTargetUseCase
import it.unito.progmob.core.domain.usecase.ValidateUsernameUseCase
import it.unito.progmob.core.domain.usecase.ValidateWeightUseCase

/**
 * Data class that holds all use cases related to the profile feature.
 */
data class ProfileUseCases (
    val validateUsernameUseCase: ValidateUsernameUseCase,
    val validateHeightUseCase: ValidateHeightUseCase,
    val validateWeightUseCase: ValidateWeightUseCase,
    val validateTargetUseCase: ValidateTargetUseCase,
    val saveUserHeightEntryUseCase: SaveUserHeightEntryUseCase,
    val saveUserWeightEntryUseCase: SaveUserWeightEntryUseCase,
    val saveUsernameEntryUseCase: SaveUsernameEntryUseCase,
    val updateTargetUseCase: UpdateTargetUseCase,
    val readUsernameEntryUseCase: ReadUsernameEntryUseCase,
    val readUserHeightEntryUseCase: ReadUserHeightEntryUseCase,
    val readUserWeightEntryUseCase: ReadUserWeightEntryUseCase,
    val getTargetUseCase: GetTargetUseCase
)
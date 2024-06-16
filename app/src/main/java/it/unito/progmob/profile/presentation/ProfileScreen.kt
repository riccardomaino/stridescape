package it.unito.progmob.profile.presentation

import ProfileDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unito.progmob.R
import it.unito.progmob.profile.presentation.components.ImageBorderAnimation
import it.unito.progmob.profile.presentation.components.ProfileButton
import it.unito.progmob.profile.presentation.components.ProfileTextField
import it.unito.progmob.profile.presentation.components.ProfileUserTextField
import it.unito.progmob.profile.presentation.state.UiProfileState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileEvent: (ProfileEvent) -> Unit,
    profileState: UiProfileState,
) {
    var isSaveButtonEnabled by remember { mutableStateOf(false) }
    val changeUserName = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    var backupUsername by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        backupUsername = profileState.username
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = medium),
            horizontalArrangement = Arrangement.spacedBy(medium, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageBorderAnimation(
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = stringResource(R.string.profile_user_icon_desription),
                borderPadding = 5.dp,
                borderWidth = 15f,
                gradientColors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary
                ),
            )
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 35.sp)
                )
                AnimatedVisibility(visible = changeUserName.value) {
                    ProfileUserTextField(
                        value = profileState.username,
                        onValueChange = {
                            profileEvent(ProfileEvent.UsernameChanged(it))
                            isSaveButtonEnabled = true
                        },
                        isError = profileState.usernameError != null,
                        errorText = profileState.usernameError,
                        iconButtonClick = {
                            changeUserName.value = false
                            profileState.username = backupUsername
                        }
                    )
                }
                AnimatedVisibility(
                    visible = !changeUserName.value,
                    exit = ExitTransition.None
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            profileState.username,
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Thin)
                        )
                        IconButton(onClick = { changeUserName.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.profile_modify_icon_description)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(medium))
        Column(
            modifier = modifier
                .padding(horizontal = large)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ProfileTextField(
                textFieldTitle = stringResource(R.string.textfield_user_height_title),
                value = profileState.height,
                suffix = stringResource(R.string.textfield_height_unit_measure),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    profileEvent(ProfileEvent.HeightChanged(it))
                    isSaveButtonEnabled = true
                },
                textAlign = TextAlign.Start,
                isError = profileState.heightError != null,
                errorText = profileState.heightError
            )
            ProfileTextField(
                textFieldTitle = stringResource(R.string.texfield_user_weight_title),
                value = profileState.weight,
                suffix = stringResource(R.string.textfield_weight_unit_measure),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    profileEvent(ProfileEvent.WeightChanged(it))
                    isSaveButtonEnabled = true
                },
                textAlign = TextAlign.Start,
                isError = profileState.weightError != null,
                errorText = profileState.weightError
            )
            ProfileTextField(
                textFieldTitle = stringResource(R.string.texfield_user_steps_target_title),
                value = profileState.target,
                suffix = stringResource(R.string.textfield_steps_unit_measure),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    profileEvent(ProfileEvent.TargetChanged(it))
                    isSaveButtonEnabled = true
                },
                textAlign = TextAlign.Start,
                isError = profileState.targetError != null,
                errorText = profileState.targetError
            )
            ProfileButton(
                text = stringResource(R.string.profile_save_changes_btn),
                onClick = {
                    profileEvent(ProfileEvent.SaveProfile)
                    isSaveButtonEnabled = false
                    openAlertDialog.value = true
                    changeUserName.value = false
                    backupUsername = profileState.username
                },
                isEnabled = isSaveButtonEnabled,
            )
        }
    }
    if (openAlertDialog.value) {
        ProfileDialog(
            onConfirmation = { openAlertDialog.value = false },
            dialogTitle = stringResource(R.string.profiledialog_title),
            dialogText = stringResource(R.string.profiledialog_text),
            onDismiss = { openAlertDialog.value = false }
        )
    }
}


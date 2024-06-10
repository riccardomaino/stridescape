package it.unito.progmob.profile.presentation

import ProfileDialog
import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.profile.presentation.components.ProfileButton
import it.unito.progmob.profile.presentation.components.ProfileTextField
import it.unito.progmob.profile.presentation.components.ProfileUserTextField
import it.unito.progmob.profile.presentation.state.UiProfileState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileEvent: (ProfileEvent) -> Unit,
    profileState: UiProfileState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    var isSaveButtonEnabled by remember { mutableStateOf(false) }
    val userCircleColor = MaterialTheme.colorScheme.onPrimary
    val iconSize = LocalConfiguration.current.screenWidthDp.dp * 0.25f
    val changeUserName = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    var backupUsername by remember { mutableStateOf(profileState.username) }
    Log.d("ProfileScreen", "Username: ${backupUsername}")
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
                .fillMaxSize(0.2f)
                .padding(horizontal = medium),
            horizontalArrangement = Arrangement.spacedBy(medium, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(large)
                ) {
                    drawCircle(
                        color = userCircleColor,
                        radius = iconSize.toPx() / 2,
                    )
                }
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(bottom = small)
                )
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Ciao,",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
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
                            if (profileState.usernameError == null) {
                                changeUserName.value = false
                                Log.d("ProfileScreen", "Username changed to $backupUsername")
                                profileState.username = backupUsername
                            }
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
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Thin)
                        )
                        IconButton(onClick = { changeUserName.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile"
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
                text = "Save Changes",
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
            dialogTitle = "Username Changed",
            dialogText = "Your personal information has been successfully updated!",
            icon = Icons.Default.CheckCircle,
            onDismiss = { openAlertDialog.value = false }
        )
    }
}


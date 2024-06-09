package it.unito.progmob.profile.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import it.unito.progmob.R
import it.unito.progmob.profile.presentation.components.ProfileButton
import it.unito.progmob.profile.presentation.components.ProfileTextField
import it.unito.progmob.profile.presentation.state.UiProfileState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileEvent: (ProfileEvent) -> Unit,
    profileState: UiProfileState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Username", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(small),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Prova",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(small)
                        )
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(horizontal = large, vertical = small),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(
                modifier = modifier
                    .padding(large)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTextField(
                    textFieldTitle = stringResource(R.string.user_height_textfield_title),
                    value = profileState.height,
                    suffix = stringResource(R.string.height_unit_measure_textfield),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        profileEvent(ProfileEvent.HeightChanged(it))
                    },
                    textAlign = TextAlign.Start,
                    isError = profileState.heightError != null,
                    errorText = profileState.heightError
                )
                Spacer(modifier = modifier.weight(0.5f))
                ProfileTextField(
                    textFieldTitle = stringResource(R.string.user_weight_textfield_title),
                    value = profileState.weight,
                    suffix = stringResource(R.string.weight_unit_measure_textfield),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        profileEvent(ProfileEvent.WeightChanged(it))
                    },
                    textAlign = TextAlign.Start,
                    isError = profileState.weightError != null,
                    errorText = profileState.weightError
                )
                Spacer(modifier = modifier.weight(0.5f))
                ProfileTextField(
                    textFieldTitle = stringResource(R.string.user_steps_target_textfield_title),
                    value = profileState.target,
                    suffix = stringResource(R.string.steps_number_textfield),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        profileEvent(ProfileEvent.TargetChanged(it))
                    },
                    textAlign = TextAlign.Start,
                    isError = profileState.targetError != null,
                    errorText = profileState.targetError
                )
                Spacer(modifier = modifier.height(large))
                ProfileButton(text = "Save changes", onClick = {profileEvent(ProfileEvent.SaveProfile)})
            }
        }
    }
}


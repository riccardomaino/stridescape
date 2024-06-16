package it.unito.progmob.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import it.unito.progmob.onboarding.presentation.OnBoardingEvent
import it.unito.progmob.onboarding.presentation.state.UiOnBoardingState
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun OnBoardingProfileTop(
    modifier: Modifier = Modifier,
    onBoardingEvent: (OnBoardingEvent) -> Unit,
    onBoardingState: UiOnBoardingState,
) {
    Column(
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        Box(
            modifier = Modifier
                .padding(large)
                .clip(RoundedCornerShape(medium))
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = large, vertical = small),
                text = stringResource(R.string.onboarding_page4_description),
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
            )
        }
        Column(
            modifier = modifier
                .padding(horizontal = large)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OnBoardingTextField(
                textFieldTitle = stringResource(R.string.textfield_username_title),
                value = onBoardingState.username,
                placeholder = stringResource(R.string.textfield_username_title),
                suffix = "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                onValueChange = {
                    onBoardingEvent(OnBoardingEvent.UsernameChanged(it))
                },
                textAlign = TextAlign.Start,
                isError = onBoardingState.usernameError != null,
                errorText = onBoardingState.usernameError
            )
            OnBoardingTextField(
                textFieldTitle = stringResource(R.string.textfield_user_height_title),
                placeholder = "170",
                value = onBoardingState.height,
                suffix = stringResource(R.string.textfield_height_unit_measure),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    onBoardingEvent(OnBoardingEvent.HeightChanged(it))
                },
                textAlign = TextAlign.Start,
                isError = onBoardingState.heightError != null,
                errorText = onBoardingState.heightError
            )
            OnBoardingTextField(
                textFieldTitle = stringResource(R.string.texfield_user_weight_title),
                value = onBoardingState.weight,
                suffix = stringResource(R.string.textfield_weight_unit_measure),
                placeholder = "70",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    onBoardingEvent(OnBoardingEvent.WeightChanged(it))
                },
                textAlign = TextAlign.Start,
                isError = onBoardingState.weightError != null,
                errorText = onBoardingState.weightError
            )
            OnBoardingTextField(
                textFieldTitle = stringResource(R.string.texfield_user_steps_target_title),
                value = onBoardingState.target,
                placeholder = "5000",
                suffix = stringResource(R.string.textfield_steps_unit_measure),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    onBoardingEvent(OnBoardingEvent.TargetChanged(it))
                },
                textAlign = TextAlign.Start,
                isError = onBoardingState.targetError != null,
                errorText = onBoardingState.targetError
            )
        }

    }
}
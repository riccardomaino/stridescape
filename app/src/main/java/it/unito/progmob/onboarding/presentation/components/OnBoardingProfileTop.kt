package it.unito.progmob.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import it.unito.progmob.R
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun OnBoardingProfileTop(
    modifier: Modifier = Modifier,
    userName: MutableState<String>,
    userHeight: MutableState<String>,
    userWeight: MutableState<String>
) {
    Column(
        modifier = modifier
            .padding(large)
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(medium))
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = large, vertical = small),
                text = stringResource(R.string.onboarding_page4_description),
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
            )
        }
        Spacer(modifier = modifier.weight(0.5f))
        OnBoardingTextField(
            textFieldTitle = "Name",
            value = userName.value,
            suffix = "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            onValueChange = { userName.value = it },
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = modifier.weight(0.5f))
        OnBoardingTextField(
            textFieldTitle = "Height",
            value = userHeight.value,
            suffix = "cm",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { userHeight.value = it },
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = modifier.weight(0.5f))
        OnBoardingTextField(
            textFieldTitle = "Weight",
            value = userWeight.value,
            suffix = "kg",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { userWeight.value = it },
            textAlign = TextAlign.Start,
        )
    }
}
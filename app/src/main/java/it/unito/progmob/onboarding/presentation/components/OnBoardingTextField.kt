package it.unito.progmob.onboarding.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun OnBoardingTextField(
    modifier: Modifier = Modifier,
    textFieldTitle: String,
    value: String = "",
    suffix: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    textAlign: TextAlign = TextAlign.Center,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp*0.3f))
            Text(
                modifier = Modifier.padding(horizontal = medium),
                text = textFieldTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp*0.3f))
        }
        Spacer(modifier = modifier.height(small))
        OutlinedTextField(
            singleLine = true,
            value = value,
            onValueChange = onValueChange,
            suffix = { Text(text = suffix, style = MaterialTheme.typography.titleMedium) },
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = textAlign),
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(medium),
            modifier = modifier.width(150.dp)
        )
    }
}


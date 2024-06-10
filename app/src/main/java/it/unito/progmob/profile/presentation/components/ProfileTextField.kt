package it.unito.progmob.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import it.unito.progmob.core.presentation.util.UiText
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun ProfileTextField(
    modifier: Modifier = Modifier,
    textFieldTitle: String,
    value: String = "",
    suffix: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    textAlign: TextAlign = TextAlign.Center,
    isError: Boolean,
    errorText: UiText?,
    showTitle: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (showTitle) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = medium),
                    text = textFieldTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        .copy(textAlign = TextAlign.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = modifier.height(small))
        }
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = medium, vertical = small),
            singleLine = true,
            value = value,
            onValueChange = onValueChange,
            suffix = { Text(text = suffix, style = MaterialTheme.typography.titleMedium) },
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = textAlign),
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(small),
            isError = isError,
            supportingText = {
                Text(
                    text = if (isError) errorText!!.asString() else "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.outlineVariant,

                )
        )
    }
}


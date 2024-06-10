package it.unito.progmob.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import it.unito.progmob.core.presentation.util.UiText
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small

@Composable
fun ProfileUserTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: UiText?,
    iconButtonClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.padding(top = small),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Start),
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
            ),
            trailingIcon = {
                IconButton(onClick = iconButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(large),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

    }
}
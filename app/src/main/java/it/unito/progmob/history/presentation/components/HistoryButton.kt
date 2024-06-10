package it.unito.progmob.history.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@Composable
fun HistoryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    iconDescription: String,
    showIcon: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = large, vertical = medium),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        if (showIcon) {
            Icon(
                icon,
                modifier = modifier.size(extraLarge),
                contentDescription = iconDescription,
                tint = textColor
            )
            Spacer(modifier = Modifier.width(small))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(vertical = small),
            color = textColor
        )
    }

}
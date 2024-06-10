package it.unito.progmob.profile.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R
import it.unito.progmob.ui.theme.extralargeRadius
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.small

@Composable
fun ProfileButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(extralargeRadius),
    ) {
        Icon(
            Icons.Filled.Save,
            modifier = Modifier.size(large),
            contentDescription = stringResource(R.string.profile_save_icon_content_desc),
            tint = if(isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.width(small))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(vertical = small)
        )
    }
}
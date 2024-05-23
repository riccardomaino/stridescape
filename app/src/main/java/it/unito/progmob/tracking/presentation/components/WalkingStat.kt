package it.unito.progmob.tracking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.small

@Composable
fun WalkingStat(modifier: Modifier = Modifier, icon: ImageVector, iconContentDescription: String, title: String, content: String, color: Color) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon,
                contentDescription = iconContentDescription,
                modifier = modifier
                    .size(extraLarge)
                    .padding(bottom = small)
                    .background(Color.Transparent),
                tint = color
            )
            Text(
                content,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
package it.unito.progmob.home.presentation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R
import it.unito.progmob.ui.theme.large

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeEvent: (HomeEvent) -> Unit) {

    val activityRecognitionPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                homeEvent(
                    HomeEvent.SavePermissionResult(
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        isGranted
                    )
                )
            }
        }
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = stringResource(R.string.home_user_icon),
            )
            Text(
                stringResource(R.string.home_title),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold)
            )
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(R.string.home_settings_icon)
            )
        }
    }
}

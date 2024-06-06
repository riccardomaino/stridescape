package it.unito.progmob.tracking.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R
import it.unito.progmob.tracking.presentation.TrackingEvent
import it.unito.progmob.ui.theme.extralargeRadius

@Composable
fun StopWalkDialog(
    modifier: Modifier = Modifier,
    trackingEvent: (TrackingEvent) -> Unit,
    shouldShowDialog: Boolean
) {
    if (shouldShowDialog) {
        AlertDialog(
            onDismissRequest = {
                trackingEvent(TrackingEvent.ShowStopWalkDialog(false))
            },
            title = {
                Text(text = "Stop Walk?")
            },
            text = {
                Text(text = "Are you sure you want to stop your walk? This will end your current tracking session.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        trackingEvent(TrackingEvent.StopTrackingService)
                        trackingEvent(TrackingEvent.ShowStopWalkDialog(false))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(extralargeRadius)
                ) {
                    Text(
                        text = stringResource(R.string.tracking_dialog_confirm_btn_text),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    trackingEvent(TrackingEvent.ShowStopWalkDialog(false))
                }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }
}
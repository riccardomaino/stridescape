package it.unito.progmob.core.presentation.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.unito.progmob.R

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (isPermanentlyDeclined) {
                    onGoToAppSettingsClick()
                    onDismiss()
                } else {
                    onOkClick()
                }
            }) {
                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(R.string.home_permissiondialog_grant_permission_btn)
                    } else {
                        stringResource(R.string.dialog_ok_btn)
                    },
                )
            }
        },
        title = {
            Text(text = stringResource(R.string.home_permissiondialog_title))
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined,
                    LocalContext.current
                )
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String
}

class ActivityRecognitionPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.home_permissiondialog_activity_recognition_declined_txt)
        } else {
            context.getString(R.string.home_permissiondialog_activity_recognition_txt)
        }
    }
}

class AccessFineLocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.home_permissiondialog_fine_location_declined_txt)
        } else {
            context.getString(R.string.home_permissiondialog_fine_location_txt)
        }
    }
}

class PostNotificationsPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.home_permissiondialog_post_notifications_declined_txt)
        } else {
            context.getString(R.string.home_permissiondialog_post_notifications_txt)
        }
    }
}
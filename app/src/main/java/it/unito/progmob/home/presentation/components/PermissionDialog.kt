package it.unito.progmob.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.unito.progmob.ui.theme.medium

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                text = if (isPermanentlyDeclined) {
                    "Grant permission"
                } else {
                    "Ok"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPermanentlyDeclined) {
                            onGoToAppSettingsClick()
                        } else {
                            onOkClick()
                        }
                    }
                    .padding(medium)
            )
        },
        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(text = permissionTextProvider.getDescription(isPermanentlyDeclined))
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class ActivityRecognitionPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "You have permanently denied the \"Activity Recognition\" permission. Please go to the app settings and grant the permission."
        } else {
            "The \"Activity Recognition\" permission is required to track your steps."
        }
    }
}

class AccessFineLocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "You have permanently denied the \"Access Fine Location\" permission. Please go to the app settings and grant the permission."
        } else {
            "The \"Access Fine Location\" permission is required to access your precise location."
        }
    }
}

class PostNotificationsPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined){
            "You have permanently denied the \"Post Notifications\" permission. Please go to the app settings and grant the permission."
        }else{
            "The \"Post Notifications\" permission is required to show you notification regarding your runs."
        }
    }
}

class AccessCourseLocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "You have permanently denied the permission. Please go to the app settings and grant the permission."
        } else {
            "This permission is required to detect your approximate location."
        }
    }
}

class AccessBackgroundLocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "You have permanently denied the permission. Please go to the app settings and grant the permission."
        } else {
            "This permission is required to access your location in the background."
        }
    }
}
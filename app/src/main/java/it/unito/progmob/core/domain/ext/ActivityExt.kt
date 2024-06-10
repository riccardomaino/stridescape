package it.unito.progmob.core.domain.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Opens the app settings for the current app.
 */
fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
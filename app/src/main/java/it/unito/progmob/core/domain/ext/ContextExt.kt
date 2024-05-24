package it.unito.progmob.core.domain.ext

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


private val allPermissions = mutableListOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.FOREGROUND_SERVICE
).apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        add(Manifest.permission.FOREGROUND_SERVICE_HEALTH)
        add(Manifest.permission.ACTIVITY_RECOGNITION)
        add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        add(Manifest.permission.ACTIVITY_RECOGNITION)
        add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }
}.toTypedArray()


/**
 * Finds the activity associated with the given context.
 *
 * @return The activity associated with the context.
 * @throws IllegalStateException if no activity can be found.
 */
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    throw IllegalStateException("No activity founded")
}


/**
 * Extension function of the Context class used to check if the application has all the required permissions.
 *
 * @return True if all permissions are granted, false otherwise.
 */
fun Context.hasAllPermissions(): Boolean = allPermissions.all {
    ContextCompat.checkSelfPermission(
        this,
        it
    ) == PackageManager.PERMISSION_GRANTED
}
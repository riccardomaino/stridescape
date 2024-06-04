package it.unito.progmob.core.domain.ext

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import it.unito.progmob.R

val Context.weekDaysNames: Array<String>
    get() = arrayOf(
        this.resources.getString(R.string.week_monday),
        this.resources.getString(R.string.week_tuesday),
        this.resources.getString(R.string.week_wednesday),
        this.resources.getString(R.string.week_thursday),
        this.resources.getString(R.string.week_friday),
        this.resources.getString(R.string.week_saturday),
        this.resources.getString(R.string.week_sunday)
    )

val Context.monthsNames: Array<String>
    get() = arrayOf(
        this.resources.getString(R.string.month_january),
        this.resources.getString(R.string.month_february),
        this.resources.getString(R.string.month_march),
        this.resources.getString(R.string.month_april),
        this.resources.getString(R.string.month_may),
        this.resources.getString(R.string.month_june),
        this.resources.getString(R.string.month_july),
        this.resources.getString(R.string.month_august),
        this.resources.getString(R.string.month_september),
        this.resources.getString(R.string.month_october),
        this.resources.getString(R.string.month_november),
        this.resources.getString(R.string.month_december)
    )



val Context.allPermissions by lazy {
    mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.ACTIVITY_RECOGNITION)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }.toTypedArray()
}




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
package it.unito.progmob.core.domain.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper


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
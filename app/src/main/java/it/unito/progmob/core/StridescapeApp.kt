package it.unito.progmob.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import it.unito.progmob.R
import it.unito.progmob.core.domain.service.TrackingService.Companion.NOTIFICATION_CHANNEL_ID
import it.unito.progmob.core.domain.service.TrackingService.Companion.NOTIFICATION_CHANNEL_NAME

@HiltAndroidApp
class StridescapeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // NotificationChannel used to show notifications.
        val trackingChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        trackingChannel.apply {
            description = getString(R.string.notification_channel_description)
        }


        // A SystemService is a Service that comes directly from Android OS. Since showing a
        // notification is something not allowed for our Application, the Android OS offers us a
        // SystemService called NotificationService that allows us to show notifications.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(trackingChannel)

    }
}
package it.unito.progmob.core

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import it.unito.progmob.core.domain.Constants.NOTIFICATION_CHANNEL_ID

@HiltAndroidApp
class StridescapeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Running Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        /*
        SystemService è un Service che proviene direttamente da Android OS. Dato che mostrare una
        notifica è un qualcosa di non consentito per la nostra Applicazione, perciò Android ci offre
        un SystemService in modo da poter ottenere questa funzionalità utilizzandolo
        */
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}
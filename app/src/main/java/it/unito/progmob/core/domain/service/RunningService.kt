package it.unito.progmob.core.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import it.unito.progmob.R
import it.unito.progmob.core.domain.Constants.NOTIFICATION_CHANNEL_ID
import it.unito.progmob.core.domain.Constants.NOTIFICATION_ID

class RunningService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.name -> start()
            Actions.STOP.name -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun start(){
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Elapsed time: 00:50")
            .build()
        startForeground(NOTIFICATION_ID , notification)
    }

    private fun stop(){
        stopSelf()
    }

    enum class Actions {
        START, STOP
    }
}
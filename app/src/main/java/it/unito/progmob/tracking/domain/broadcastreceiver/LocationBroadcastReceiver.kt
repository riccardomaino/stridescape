package it.unito.progmob.tracking.domain.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log

/**
 * A [BroadcastReceiver] that listens for changes in location provider states (GPS and Network) and
 * notifies a [LocationSettingsListener] accordingly.
 *
 * @param locationSettingsListener The listener to be notified of location setting changes.
 */
class LocationBroadcastReceiver(
    private val locationSettingsListener: LocationSettingsListener
) : BroadcastReceiver() {
    private var isFirstEnableReceive = true
    private var isFirstDisableReceive = true
    private var isGpsEnabled: Boolean = false
    private var isNetworkEnabled: Boolean = false

    /**
     * Receives the broadcast and handles location provider changes. If both GPS and Network
     * providers are enabled, it notifies the listener that location settings are enabled. If either
     * provider is disabled, it notifies the listener that location settings are disabled.
     *
     * @param context The context of the application.
     * @param intent The intent received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.let { action ->
            if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (isGpsEnabled && isNetworkEnabled) {
                    if(isFirstEnableReceive) {
                        locationSettingsListener.onEnabled()
                        isFirstEnableReceive = false
                        isFirstDisableReceive = true
                    }
                } else {
                    if(isFirstDisableReceive) {
                        locationSettingsListener.onDisabled()
                        isFirstDisableReceive = false
                        isFirstEnableReceive = true
                    }
                }
            }
        }
    }
}
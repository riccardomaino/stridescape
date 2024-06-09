package it.unito.progmob.tracking.domain.broadcastreceiver

/**
 * A listener interface for changes in location provider states.
 */
interface LocationSettingsListener {
    fun onEnabled()
    fun onDisabled()
}
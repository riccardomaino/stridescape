package it.unito.progmob.core.domain.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Abstract class representing an Android sensor. This class provides functionality for checking
 * sensor availability, starting and stopping sensor listening, and handling sensor value changes.
 *
 * @param context The Android context.
 * @param sensorFeature The system feature associated with the sensor.
 * @param sensorType The type of the sensor.
 */
abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
): MeasurableSensor(sensorType), SensorEventListener {

    /**
     * Checks whether the sensor is available on the device.
     */
    override val doesSensorExists: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    // System service that report the sensor values, it is a late int because we will be initialized
    // the first time the startListening function is called
    private lateinit var sensorManager: SensorManager

    // Returned from the sensor manager when we start to listen to it and we say which sensor
    // we want to listen to
    private var sensor: Sensor? = null

    /**
     * Starts listening to the sensor.
     */
    override fun startListening() {
        // If the sensor doesn't exists we just return form the function
        if(!doesSensorExists){
            return
        }
        // Initialize the sensor manager and sensor if they are not already initialized (first time
        // we call this function).
        if(!this::sensorManager.isInitialized && sensor == null) {
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        // Register the listener to the sensor, if the sensor is not null
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    /**
     * Function that stop listening to the sensor, it unregisters the
     * listener from the sensorManager
     * */
    override fun stopListening() {
        // if the sensor doesn't exists or the sensor manager is not initialized we just return
        // form the function
        if(!this.doesSensorExists || !this::sensorManager.isInitialized){
            return
        }
        // Now we unregister the listener from the sensor
        sensorManager.unregisterListener(this)
    }

    /**
     * Called when the sensor value changes
     * @param event - the sensor event that has been triggered
     * */
    override fun onSensorChanged(event: SensorEvent?) {
        // If the sensor doesn't exists we just return form the function
        if(!doesSensorExists){
            return
        }
        // If the event is from the sensor we want to handle, we get the values from the event and
        // pass them to the onSensorValuesChanged function
        if(event?.sensor?.type == sensorType){
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    /**
     * Handles changes in sensor accuracy.
     *
     * @param sensor The sensor.
     * @param accuracy The new accuracy.
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

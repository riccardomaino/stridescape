package it.unito.progmob.core.stepscounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 *  Second layer of abstraction to being able to access any type of sensor (light, proximity, etc),
 *  this class implements every function needed to get the sensor values
 *  @param context - application context to get the instance of the sensor manager
 *  @param sensorFeature - used to determine whether the sensor is available or not
 *  @param sensorType - the type of sensor
 */
abstract class AndroidSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
): MeasurableSensor(sensorType), SensorEventListener {

    // Function to check whether the sensor is available or not
    override val doesSensorExists: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    // System service that report the sensor values, it is a late int because we will be initialized
    // the first time the startListening function is called
    private lateinit var sensorManager: SensorManager

    // Returned from the sensor manager when we start to listen to it and we say which sensor
    // we want to listen to
    private var sensor: Sensor? = null

    /**
     * Function that start listening to the sensor
     * */
    override fun startListening() {
        // if the sensor doesn't exists we just return form the function
        if(!doesSensorExists){
            return
        }
        //Now we check if this is the first time we call the "startListening" function, if so the
        // sensor manager and the sensor are not initialized
        if(!::sensorManager.isInitialized && sensor == null) {
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        // Now we register the listener to the sensor, if the sensor is not null
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
        if(!doesSensorExists || !::sensorManager.isInitialized){
            return
        }
        // Now we unregister the listener from the sensor
        sensorManager.unregisterListener(this)
    }

    /**
     * Function that is called when the sensor value changes
     * @param event - the sensor event that has been triggered
     * */
    override fun onSensorChanged(event: SensorEvent?) {
        // if the sensor doesn't exists we just return form the function
        if(!doesSensorExists){
            return
        }
        // if the event is from the sensor we want to handle, we get the values from the event and
        // pass them to the onSensorValuesChanged function
        if(event?.sensor?.type == sensorType){
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    /**
     * FUnction used to handle the accuracy of the sensor
     * */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

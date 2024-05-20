package it.unito.progmob.core.stepscounter

/**
 * Sensor class used to get the values from the sensor
 * @param sensorType: Int - The type of sensor
 */
abstract class MeasurableSensor(
    protected val sensorType: Int
) {

    // When the sensor value changes, this event is fired, this is a List because
    // some sensors may give us multiple values
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null

    abstract val doesSensorExists: Boolean

    // Functions to start and stop listening to sensor changes
    abstract fun startListening()
    abstract fun stopListening()

    // Function to set the listener
    /**
     * Sets the listener for the sensor
     * @param listener: (List<Float>) -> Unit - The listener to be set
     * */
    fun setOnSensorValueChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }

}
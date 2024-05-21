package it.unito.progmob.core.stepscounter

/**
 * Abstract class representing a measurable sensor.
 *
 * This class provides functionality for setting a listener to receive sensor value changes,
 * checking sensor availability, and starting and stopping sensor listening.
 *
 * @param sensorType The type of the sensor.
 */
abstract class MeasurableSensor(
    protected val sensorType: Int
) {

    /**
     * Event fired when the sensor value changes.
     * This is a list because some sensors may provide multiple values.
     */
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null

    /**
     * Checks whether the sensor is available on the device.
     */
    abstract val doesSensorExists: Boolean

    /**
     * Starts listening to the sensor.
     */
    abstract fun startListening()

    /**
     * Stops listening to the sensor.
     */
    abstract fun stopListening()

    /**
     * Sets the listener for the sensor.
     *
     * @param listener - The listener to be set.
     */
    fun setOnSensorValueChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }

}
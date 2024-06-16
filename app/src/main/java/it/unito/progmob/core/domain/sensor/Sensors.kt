package it.unito.progmob.core.domain.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

/**
 * A wrapper class for the step counter sensor, providing access to step count data.
 *
 * @param context The application context.
 */
class StepCounterSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_STEP_COUNTER,
    sensorType = Sensor.TYPE_STEP_COUNTER
)

/**
 * A wrapper class for the accelerometer sensor, providing access to acceleration data.
 *
 * @param context The application context.
 */
class AccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)




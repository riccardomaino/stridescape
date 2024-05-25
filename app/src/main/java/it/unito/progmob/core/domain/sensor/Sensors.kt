package it.unito.progmob.core.domain.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class StepCounterSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_STEP_COUNTER,
    sensorType = Sensor.TYPE_STEP_COUNTER
)

class AccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)

/*
private fun testSensors(context: Context) {
    val stepsStepCounterSensor = 0
    val stepsAccelerometerSensor = 0

    val magnitudePrevious = 0.0f

    stepCounterSensor.startListening()
    accelerometerSensor.startListening()

    stepCounterSensor.setOnSensorValueChangedListener { values ->
        val numSteps = values[0].toInt()
        stepsStepCounterSensor = numSteps
    }

    accelerometerSensor.setOnSensorValueChangedListener { values ->
        val x: Float = values[0]
        val y: Float = values[1]
        val z: Float = values[2]

        // WITHOUT GRAVITY
        val magnitude = sqrt(x.pow(2.0f) + y.pow(2.0f) + z.pow(2.0f))

        // WITH EARTH'S GRAVITY INCLUDED
        // val magnitude = (sqrt(x.pow(2) + y.pow(2) + z.pow(2))) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        val magnitudeDelta = magnitude - magnitudePrevious
        magnitudePrevious = magnitude.toDouble()

        if (magnitudeDelta > 6) { // adjust the threshold based on your device
            stepsAccelerometerSensor++
        }
    }
}
*/

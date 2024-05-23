package it.unito.progmob.tracking.domain.stepscounter

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
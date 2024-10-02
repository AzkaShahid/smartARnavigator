package com.app.ar

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent

fun interface RotationListener {
    companion object {
        private val rotationListeners = mutableListOf<RotationListener>()

        fun register(rotationListener: RotationListener) {
            if (rotationListeners.all { it != rotationListener }) {
                rotationListeners.add(rotationListener)
            }
        }

        fun unregister(rotationListener: RotationListener) {
            rotationListeners.remove(rotationListener)
        }

        fun trigger(rotationVector: SensorEvent) {
            rotationListeners.forEach {
                it.onRotationVectorChanged(rotationVector)
            }
        }
    }

    fun onRotationVectorChanged(rotationVector: SensorEvent)
}

class RotationHandler constructor(val activity: Activity) {
    private var compass: Compass? = null

    public fun start(){
        compass = Compass(activity)
        compass!!.setListener(object : Compass.CompassListener{
            override fun onNewAzimuth(azimuth: Float, event: SensorEvent?) {
                if (event!!.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                    RotationListener.trigger(event)
                }
            }

        })
        compass!!.start(activity)
    }
}
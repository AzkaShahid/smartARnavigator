package com.app.ar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.app.R;


public class Compass implements SensorEventListener {
    private static final String TAG = Compass.class.getSimpleName();

    public interface CompassListener {
        void onNewAzimuth(float azimuth, SensorEvent event);
    }

    private CompassListener listener;

    private SensorManager sensorManager;
    private Sensor asensor;
    private Sensor msensor;
    private Sensor rotation;

    private float[] aData = new float[3];
    private float[] mData = new float[3];
    private float[] R_ = new float[9];
    private float[] I = new float[9];

    private float azimuthFix;

    public Compass(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;

        asensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void start(Context context) {
        try {
            sensorManager.registerListener(this, asensor,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, msensor,
                    SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, rotation,
                    SensorManager.SENSOR_DELAY_GAME);

            PackageManager manager = context.getPackageManager();
            boolean haveAS = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
            boolean haveCS = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);

            if (!haveAS || !haveCS) {
                sensorManager.unregisterListener(this, asensor);
                sensorManager.unregisterListener(this, msensor);
                sensorManager.unregisterListener(this, rotation);
                Log.e(TAG, "Device don't have enough sensors");
                dialogError(context);
            }
        } catch (Exception ex){

        }
    }

    private void dialogError(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("");
        builder.setNegativeButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float fix) {
        azimuthFix = fix;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(CompassListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        float bearing = 0;
        float azimuth = 0;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                aData[0] = alpha * aData[0] + (1 - alpha)
                        * event.values[0];
                aData[1] = alpha * aData[1] + (1 - alpha)
                        * event.values[1];
                aData[2] = alpha * aData[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mData[0] = alpha * mData[0] + (1 - alpha)
                        * event.values[0];
                mData[1] = alpha * mData[1] + (1 - alpha)
                        * event.values[1];
                mData[2] = alpha * mData[2] + (1 - alpha)
                        * event.values[2];
            }

            boolean success = SensorManager.getRotationMatrix(R_, I, aData, mData);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R_, orientation);
                bearing = (float) Math.toDegrees(orientation[0]);
                azimuth = (bearing + azimuthFix + 360) % 360;

            }
        }
        if (listener != null) {
            listener.onNewAzimuth(azimuth, event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}


package android.mission.accidentdetection.Listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by Annick on 03/02/2017.
 */

public class SensorListener implements SensorEventListener {

    public SensorListener() {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //If accelerometer data
            //acceltxt.setText("Accelerometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
            Log.d("sensor", "Accelerometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //If gyro data
            //gyrotxt.setText("Gyrometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
            Log.d("sensor",event.values[0] + " " +event.values[1] + " " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing here.
    }
}

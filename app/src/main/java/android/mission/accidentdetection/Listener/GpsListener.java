package android.mission.accidentdetection.Listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Annick on 03/02/2017.
 */

public class GpsListener extends AppCompatActivity implements android.location.LocationListener, SensorEventListener {

    private Context contexte;
    private float vitesse;
    GpsCallBack gpsCallBack;


    public GpsListener(Context context, GpsCallBack gpsCallBack) {
        contexte = context;
        this.gpsCallBack = gpsCallBack;
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

        /** Sending Event */
        gpsCallBack.onSensorEventRecieved(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing here.
    }

    @Override
    public void onLocationChanged(Location location) {
        /** * 3,6 to put the speed in kilometers*/
        vitesse = location.getSpeed();
        vitesse = vitesse * (float) 3.6;

        Log.d("speed", "speed"+ vitesse);

        gpsCallBack.onSpeedRecieved(vitesse);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexte);
        builder.setMessage("You need to activate your geolocalisation to use this application. Please turn on the access to your position in Settings.")
                .setTitle("Unable to get your position")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                contexte.startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ((Activity) contexte).finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    public interface GpsCallBack {
        void onSpeedRecieved(Float vitesse);
        void onSensorEventRecieved (SensorEvent event);
    }
}

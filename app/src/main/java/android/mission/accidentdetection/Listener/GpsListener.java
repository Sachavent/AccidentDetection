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

import java.util.Timer;

/**
 * Created by Annick on 03/02/2017.
 */

public class GpsListener extends AppCompatActivity implements android.location.LocationListener, SensorEventListener {

    private Context contexte;
    private float vitesse;
    GpsCallBack gpsCallBack;

    private int acc_flag = 0;
    private int gyr_flag = 0;
    private int vit_flag = 0;
    private Timer t;

    public GpsListener(Context context, GpsCallBack gpsCallBack) {
        contexte = context;
        this.gpsCallBack = gpsCallBack;

        /* t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try{
                    //Do Calculation every 1 sec


                }catch (Exception e) {
                }
            }
        }, 1000, 1000);*/

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //If accelerometer data
            if (event.values[0]+event.values[1]+event.values[2] > 10){
                acc_flag = 1;
            }
            Log.d("sensor", "Accelerometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //If gyro data
            if (event.values[0]+event.values[1]+event.values[2] > 10){
                gyr_flag = 1;
            }
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
        float new_vitesse = location.getSpeed();
        new_vitesse = new_vitesse * (float) 3.6;

        if (vitesse > new_vitesse) {
            vit_flag = 1;
        }

        vitesse = new_vitesse;

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

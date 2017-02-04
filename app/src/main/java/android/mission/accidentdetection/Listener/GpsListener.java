package android.mission.accidentdetection.Listener;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Annick on 03/02/2017.
 */

public class GpsListener extends AppCompatActivity implements android.location.LocationListener, SensorEventListener {

    private Context contexte;

    //Sensor manager
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    float gravity[] = {0,0,(float)9.81};

    // Location
    private LocationManager locationManager;

    private float last_speed;
    GpsCallBack gpsCallBack;

    private int acc_flag = 0;
    private int gyr_flag = 0;
    private int vit_flag = 0;
    private Timer t;

    public GpsListener(Context context, GpsCallBack gpsCallBack) {
        contexte = context;

        //Register
        mSensorManager = (SensorManager) contexte.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Lance le service de localisation
        locationManager = (LocationManager) contexte.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(contexte, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexte, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mSensorManager.registerListener(this, accelerometer, 1000000);
        mSensorManager.registerListener(this, gyroscope, 1000000);

        this.gpsCallBack = gpsCallBack;
    }


    public void calculChoc() {
        //Do Calculation every 1 sec
        gpsCallBack.onChocEventRecieved((float)0.1);
        Log.d("choc", "YOP");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
           if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

               // alpha is calculated as t / (t + dT)
               // with t, the low-pass filter's time-constant
               // and dT, the event delivery rate
               float alpha = (float)0.8;
               gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
               gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
               gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
               float linear_acceleration[] = {
                    event.values[0] - gravity[0],
                    event.values[1] - gravity[1],
                    event.values[2] - gravity[2]
               };

               //If accelerometer data
            if (linear_acceleration[0]+linear_acceleration[1]+linear_acceleration[2] > 3){
                acc_flag = 1;
                calculChoc();
            }
            Log.d("sensor", "Accelerometer :\nx:"+linear_acceleration[0] + "\ny:" +linear_acceleration[1] + "\nz:" + linear_acceleration[2]);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //If gyro data
            if (event.values[0]+event.values[1]+event.values[2] > 10){
                gyr_flag = 1;
            }
            Log.d("sensor", "Gyrometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
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

        if (last_speed > new_vitesse) {
            vit_flag = 1;
        }

        last_speed = new_vitesse;

        Log.d("speed", "speed"+ new_vitesse);
        gpsCallBack.onSpeedRecieved(new_vitesse);
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
        void onChocEventRecieved(Float choc);
    }
}

package android.mission.accidentdetection.Fragments;

import android.location.LocationManager;
import android.mission.accidentdetection.Listener.LocationListener;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.TextView;

//Import for device sensors
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Annick on 03/02/2017.
 */

public class FirstFragment extends Fragment implements SensorEventListener {

    //Just textview
    TextView acceltxt;
    TextView gyrotxt;

    //Sensor manager
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    // Location
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Lance le service de localisation
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
    }

    public static FirstFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        FirstFragment firstFragment = new FirstFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onStart() {
        super.onStart();

        acceltxt = (TextView)getActivity().findViewById(R.id.acceltxtID);
        gyrotxt = (TextView)getActivity().findViewById(R.id.gyrotxtID);

        /** Using GPS */
        locationListener = new LocationListener(getContext());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }


    @Override
    public void onPause() {
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, gyroscope);
        super.onPause();
    }

    @Override
    public void onResume() {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //If accelerometer data
            acceltxt.setText("Accelerometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
            //Log.d("accel","accelerometer : x:"+event.values[0] + "; y:" +event.values[1] + "; z:" + event.values[2]);
        }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            //If gyro data
            gyrotxt.setText("Gyrometer :\nx:"+event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
            //Log.d("gyro",event.values[0] + " " +event.values[1] + " " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing here.
    }


}

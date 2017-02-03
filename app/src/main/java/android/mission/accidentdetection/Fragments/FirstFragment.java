package android.mission.accidentdetection.Fragments;

import android.hardware.SensorEvent;
import android.location.LocationManager;
import android.mission.accidentdetection.Listener.GpsListener;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Import for device sensors
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Annick on 03/02/2017.
 */

public class FirstFragment extends Fragment {

    //Just textview
    TextView acceltxt;
    TextView gyrotxt;

    //Sensor manager
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    // Location
    private LocationManager locationManager;

    // Listener
    private GpsListener gpsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Lance le service de localisation
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        gpsListener = new GpsListener(getContext(), new GpsListener.GpsCallBack() {
            @Override
            public void onSpeedRecieved(Float vitesse) {
                Log.d("vitesse", "vitesse: "+vitesse);
            }

            @Override
            public void onSensorEventRecieved(SensorEvent event) {
                Log.d("capteur", "capteur: "+event);
            }
        });
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

    }
    

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(gpsListener, accelerometer);
        mSensorManager.unregisterListener(gpsListener, gyroscope);
        super.onPause();
    }

    @Override
    public void onResume() {
        mSensorManager.registerListener(gpsListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(gpsListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }



}

package android.mission.accidentdetection.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.location.LocationManager;
import android.mission.accidentdetection.Listener.GpsListener;
import android.mission.accidentdetection.R;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Import for device sensors
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;



/**
 * Created by Annick on 03/02/2017.
 */

public class FirstFragment extends Fragment {

    //Just textview
    TextView acceltxt;
    TextView gyrotxt;

    // Listener
    private GpsListener gpsListener;

    // Permission
    private boolean permissionsEnabled;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check permission
        ActivatePermissions();
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

        acceltxt = (TextView) getActivity().findViewById(R.id.acceltxtID);
        gyrotxt = (TextView) getActivity().findViewById(R.id.gyrotxtID);

        /** Using GPS */
        gpsListener = new GpsListener(getContext(), new GpsListener.GpsCallBack() {
            @Override
            public void onSpeedRecieved(Float vitesse) {
                Log.d("vitesse", "vitesse: " + vitesse);
            }

            @Override
            public void onSensorEventRecieved(SensorEvent event) {
                Log.d("capteur", "capteur: " + event);
            }
        });
    }


    @Override
    public void onPause() {
        //mSensorManager.unregisterListener(gpsListener, accelerometer);
        //mSensorManager.unregisterListener(gpsListener, gyroscope);
        super.onPause();
    }

    @Override
    public void onResume() {
        //mSensorManager.registerListener(gpsListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(gpsListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsEnabled = true;
                } else {
                    Log.d("test ", "position refusée");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Activating position permissions is mandatory");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivatePermissions();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }

        }
    }

    private void ActivatePermissions() {
        if (permissionsEnabled != true) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, 10);
        }
    }

}

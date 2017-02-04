package android.mission.accidentdetection.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.hardware.SensorEvent;
import android.location.LocationManager;
import android.mission.accidentdetection.Intent.SmsDeliever;
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
import android.widget.ImageView;
import android.widget.TextView;

//Import for device sensors
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Annick on 03/02/2017.
 */

public class FirstFragment extends Fragment {

    //Just textview
    TextView acceltxt;
    TextView gyrotxt;
    ImageView egg;

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
        egg = (ImageView) getActivity().findViewById(R.id.eggimgID);

        /** Using GPS */
        gpsListener = new GpsListener(getContext(), new GpsListener.GpsCallBack() {
            @Override
            public void onSpeedRecieved(Float vitesse) {
                Log.d("vitesse", "vitesse: " + vitesse);
            }

            @Override
            public void onSensorEventRecieved(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                    Log.d("sensor", "Accelerometer :\nx:" + event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
                }else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                    Log.d("sensor", "Gyrometer :\nx:" + event.values[0] + "\ny:" +event.values[1] + "\nz:" + event.values[2]);
                }
            }

            @Override
            public void onChocEventRecieved(Float AccidentProba) {

                acceltxt.setText("Probabilitée d'accident : " + Math.floor(AccidentProba * 100) / 100  +"%");

                //If AccidentProba is hight
                if(AccidentProba >  50) {

                    //Show toast
                    CharSequence text = "BOUM ! t'est mort :D";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();

                    //Send emergence sms
                    ArrayList<String> phoneNumber = new ArrayList<>();
                    phoneNumber.add("0678681496");
                    //phoneNumber.add("0667391286");
                    String contacName = "Tanguy";

                    String smsBody = contacName + " Viens d'avoir un terrible accident ! D:";
                    SmsDeliever smsDeliever = new SmsDeliever(getContext(), phoneNumber, smsBody);
                    smsDeliever.SendingMessage();
                }
            }

            @Override
            public void onWarningEventRecieved(Float AccidentProba) {
                acceltxt.setText("Probabilitée d'accident : " + Math.floor(AccidentProba * 100) / 100  +"%");
                gyrotxt.setText("WARNING GPS data are more that 30sec old /!\\");

                Matrix matrix = new Matrix();
                egg.setScaleType(ImageView.ScaleType.MATRIX);   //required
                matrix.postRotate((float) AccidentProba, 0, 0);
                egg.setImageMatrix(matrix);
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
